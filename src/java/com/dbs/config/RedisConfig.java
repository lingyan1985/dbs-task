package com.dbs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Configuration
public class RedisConfig {
    // default connection timeout for initialize
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;

    @Bean
    public JedisPool getJedisPool(@Value("${redis.host}") String host,
                                  @Value("${redis.password}") String password,
                                  @Value("${redis.port}") String portString,
                                  @Value("${redis.timeout.connection}") String conTimeout) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(32);
        config.setMaxIdle(32);
        config.setMinIdle(16);
        try{
            int connectionTimeout = !StringUtils.isEmpty(conTimeout) ? Integer.parseInt(conTimeout) : DEFAULT_CONNECTION_TIMEOUT;
            int port = Integer.parseInt(portString);
            log.info("host: {}, port: {}, password: {}", host, port, password);

            JedisPool jedisPool = new JedisPool(config, host, port, connectionTimeout, password);
            return jedisPool;
        }catch (Exception e){
            // if there is an exception, means that init failed
            log.error("RRB init failed, message: {}, error: {}", e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }
}
