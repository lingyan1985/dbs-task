package com.dbs.controller;


import com.dbs.service.GitService;
import com.dbs.utils.APIResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "exposed api")
@RestController
public class GitController {
    /*private final GitService gitService;
    public GitController (GitService gitService) {
        this.gitService = gitService;
    }*/


    private final GitService gitService;

    public GitController(GitService gitService) {
        this.gitService = gitService;
    }


    @GetMapping("/test")
    public String test() {

        return "hello";
    }

    @GetMapping("/repositories/{owner}/{repository-name}")
    public ResponseEntity<?> getRepositoryInfo(@Parameter(description = "repository owner") @PathVariable("owner") String owner,
                                               @Parameter(description = "repository name") @PathVariable("repository-name") String repoName){

        if (!StringUtils.hasLength(owner) ||
                !StringUtils.hasLength(repoName)) {
            return new ResponseEntity<>(new APIResponse("error", "Fields missing"), HttpStatus.OK);
        }

        return gitService.getRepoInfo(owner, repoName);

    }

}
