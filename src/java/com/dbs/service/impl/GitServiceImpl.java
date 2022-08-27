package com.dbs.service.impl;

import com.alibaba.fastjson.JSON;
import com.dbs.repository.RedisRepository;
import com.dbs.service.GitService;
import com.dbs.utils.APIResponse;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.function.Supplier;

import static com.dbs.common.Constants.REPOS;
import static com.dbs.common.Constants.SEPARATOR;
import static com.dbs.common.Constants.SUCCESS_STATUS;
import static java.util.Objects.nonNull;


@Slf4j
@Service
public class GitServiceImpl implements GitService {
    private final Supplier<RestTemplate> restTemplateSupplier = RestTemplate::new;

    private final String gitHubUrl;
    private final RedisRepository redisRepository;

    public GitServiceImpl(@Value("${github-url}") String gitHubUrl, RedisRepository redisRepository) {
        this.gitHubUrl = gitHubUrl;
        this.redisRepository = redisRepository;
    }

    @Override
    public ResponseEntity getRepoInfo(String owner, String repoName) {
        try {
            //log.info(gitHubUrl);
            String endpointUrl = gitHubUrl + SEPARATOR + REPOS + SEPARATOR + owner + SEPARATOR + repoName;
            log.info("endpointUrl: " + endpointUrl);
            //If found in Redis, then retrieve from Redis
            String configKey = owner + "|" + repoName;
            if(redisRepository.hasKey(configKey)) {
                return new ResponseEntity<>(new APIResponse("success", redisRepository.getValue(owner+"|"+repoName)), HttpStatus.OK);
            } else {
                ResponseEntity<String> responseEntity = sendRestCall(endpointUrl, HttpMethod.GET, null, String.class);
                if (nonNull(responseEntity) && SUCCESS_STATUS.equals(responseEntity.getStatusCode().value())) {
                    if (nonNull(responseEntity.getBody())) {
                        JSONObject json = JSON.parseObject(responseEntity.getBody());
                        JSONObject gitRepoJson = new JSONObject();
                        gitRepoJson.put("fullName", json.getString("full_name"));
                        gitRepoJson.put("cloneUrl", json.getString("clone_url"));
                        gitRepoJson.put("description", json.getString("description"));
                        gitRepoJson.put("stars", json.getInteger("stargazers_count"));
                        gitRepoJson.put("createdAt", json.getString("created_at"));
                        redisRepository.setValue(owner + "|" + repoName, gitRepoJson.toJSONString());
                        return new ResponseEntity<>(new APIResponse("success", gitRepoJson), HttpStatus.OK);
                    }
                    return new ResponseEntity<>(new APIResponse("success", "No such repository"), HttpStatus.OK);
                }
            }
        } catch (Exception e) {
            log.error("Exception during getRepoInfo: {}", e.getMessage());
            return new ResponseEntity<>(new APIResponse("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new APIResponse("error", "Failed to get the response"), HttpStatus.NOT_FOUND);

    }

    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 500))
    private ResponseEntity sendRestCall(String url, HttpMethod httpMethod, HttpEntity<String> httpEntity, Class<String> clazz) {
        final RestTemplate restTemplate = restTemplateSupplier.get();
        try {
            return restTemplate.exchange(url, httpMethod, httpEntity, clazz);
        } catch (Exception e) {
            log.error("Exception when sendRestCall: {}", e.getMessage());
        }
        return new ResponseEntity<>(new APIResponse("error", "Failed to get the response"), HttpStatus.NOT_FOUND);
    }
}
