package com.dbs.service;

import org.springframework.http.ResponseEntity;

public interface GitService {
    ResponseEntity getRepoInfo(String owner, String repoName);
}
