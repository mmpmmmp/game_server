package com.wre.game.api.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisPoolConfig {

    //自动注入redis配置属性文件
    @Autowired
    private RedisProperties properties;
    @Value("${spring.redis.password}")
    private String password;

    @Bean(name = "jedisPool", destroyMethod = "close")
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        config.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());

        JedisPool pool = null;
        if (StringUtils.isBlank(password)) {
            pool = new JedisPool(config, properties.getHost(), properties.getPort(), 2000);
        } else {
            pool= new JedisPool(config, properties.getHost(), properties.getPort(), 2000, password);
        }
        return pool;
    }
}