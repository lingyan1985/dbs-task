package com.dbs.repository;

import com.alibaba.fastjson.JSONObject;

public interface RedisRepository {
    boolean hasKey(String configKey);
    void setValue(String configKey, String configValue);
    JSONObject getValue(String configKey);
}
