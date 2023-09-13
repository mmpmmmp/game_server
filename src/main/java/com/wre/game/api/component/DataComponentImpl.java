package com.wre.game.api.component;

import com.wre.game.api.constants.RedisConstant;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.exception.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class DataComponentImpl implements DataComponent {


    private static final Logger logger = LoggerFactory.getLogger(DataComponentImpl.class);

    @Value("${backend.api.token.secret}")
    private String tokenSecret;

    @Value("${backend.api.token.expire.millis}")
    private long tokenExpireMillis;

    @Value("${backend.api.token.redis.expire.second}")
    private int redisExpireSecond;

    @Autowired
    @Qualifier("jedisPool")
    private JedisPool jedisPool;

    @Override
    public void setGameData(String uuid, String appId, String data) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(RedisConstant.PREFIX_KEY_DATA + uuid, appId, data);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }

    }

    @Override
    public String getGameData(String uuid, String appId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String data="";
            data = jedis.hget(RedisConstant.PREFIX_KEY_DATA + uuid, appId);
            return data;
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void delGameDataKey(String uuid, String appId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hdel(RedisConstant.PREFIX_KEY_DATA + uuid, appId);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public String getGameConfigJson() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String data = jedis.get(RedisConstant.KEY_DATA_CONFIG);
            return data;
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setGameConfigJson(String data) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(RedisConstant.KEY_DATA_CONFIG, data);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public String getProductListJson() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String data = jedis.get(RedisConstant.KEY_DATA_PRODUCT_LIST);
            return data;
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setProductListJson(String data) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(RedisConstant.KEY_DATA_PRODUCT_LIST, data);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public String getIpConfigJson() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String data = jedis.get(RedisConstant.KEY_DATA_CONFIG_IP);
            return data;
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setIpConfigJson(String data) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(RedisConstant.KEY_DATA_CONFIG_IP, data);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setGameRankData(String appId, String data, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.setex(RedisConstant.PREFIX_KEY_DATA_RANK + appId, seconds, data);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public String getGameRankData(String appId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String data = jedis.get(RedisConstant.PREFIX_KEY_DATA_RANK + appId);
            return data;
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setAppAccessToken(String appId, String data, int seconds) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = RedisConstant.PREFIX_KEY_DATA_ACCESSTOKEN + appId;
            jedis.set(key, data, "NX", "EX", seconds);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public String getAppAccessToken(String appId) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String data = jedis.get(RedisConstant.PREFIX_KEY_DATA_ACCESSTOKEN + appId);
            return data;
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void setRequestExpireConfigJson(String requestExpireConfig) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(RedisConstant.KEY_DATA_CONFIG_REQUEST, requestExpireConfig);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public String getRequestExpireJson() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String data = jedis.get(RedisConstant.KEY_DATA_CONFIG_REQUEST);
            return data;
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }
}
