package com.dbs.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static java.util.Objects.nonNull;

@Slf4j
@Repository
public class RedisRepositoryImpl implements RedisRepository{

    private final JedisPool jedisPool;

    public RedisRepositoryImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void setValue(String configKey, String configValue){
        //Set value to Redis
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(configKey.getBytes(), configValue.getBytes());
        } catch (Exception ex)  {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public JSONObject getValue(String configKey){
        //Get value from Redis
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            byte[] valueByte = nonNull(configKey) ? jedis.get(configKey.getBytes()) : null;
            if (valueByte != null){
                return JSON.parseObject(new String(valueByte));
            }
            log.debug(String.format("Redis get configuration for key %s is Empty", configKey));
            return null;
        } catch (Exception ex)  {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
            return null;
        } finally {
            closeJedis(jedis);
        }
    }

    public boolean hasKey(String configKey) {
        //Check if key exist in Redis
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return nonNull(configKey) ? jedis.exists(configKey) : false;
        } catch (Exception ex)  {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
            return false;
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * to release jedis resource
     * attention: please release jedis resource by this after call any redis api
     * @param jedis
     */
    private void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }
}
