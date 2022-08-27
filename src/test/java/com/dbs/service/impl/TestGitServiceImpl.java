package com.dbs.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dbs.repository.RedisRepository;
import com.dbs.service.GitService;
import com.dbs.utils.APIResponse;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TestGitServiceImpl {
    private GitService gitService;
    @Mock
    private RedisRepository redisRepository;

    @Before
    public void init() {
        this.gitService = new GitServiceImpl("https://api.github.com", redisRepository);
    }

    @Test
    public void getRepoInfoGitHubUrlNull() {
        this.gitService = new GitServiceImpl("", redisRepository);
        when(redisRepository.hasKey(any())).thenReturn(false);
        ResponseEntity<String> responseEntity = gitService.getRepoInfo(any(), any());
        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void getRepoInfoNotFoundOwner() {
        when(redisRepository.hasKey(any())).thenReturn(false);
        ResponseEntity<String> responseEntity = gitService.getRepoInfo("lingyan2000", "test1");
        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getRepoInfoNotFoundRepo() {
        ResponseEntity<String> responseEntity = gitService.getRepoInfo("lingyan1985", "test1");
        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.NOT_FOUND));
    }

    @Test
    public void getRepoInfoHasRedisKey() {
        JSONObject jsonObject = composeJSONObject();
        when(redisRepository.hasKey(any())).thenReturn(true);
        when(redisRepository.getValue(any())).thenReturn(jsonObject);
        ResponseEntity<String> responseEntity = gitService.getRepoInfo("lingyan1985", "test");
        verify(redisRepository, times(0)).setValue(any(), any());
        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.OK));
    }

    @Test
    public void getRepoInfoWithoutRedisKey() {
        when(redisRepository.hasKey(any())).thenReturn(false);
        ResponseEntity<String> responseEntity = gitService.getRepoInfo("lingyan1985", "test");
        verify(redisRepository, times(1)).setValue(any(), any());
        MatcherAssert.assertThat(responseEntity.getStatusCode(), Matchers.is(HttpStatus.OK));
    }

    private JSONObject composeJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fullName", "lingyan1985");
        jsonObject.put("cloneUrl", "testCloneUrl");
        jsonObject.put("description", "This is testing");
        jsonObject.put("stars", 1);
        jsonObject.put("createdAt", "2022-08-26");
        return jsonObject;
    }
}
