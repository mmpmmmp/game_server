package com.wre.game.api.component;

import com.wre.game.api.constants.ApiConstants;
import com.wre.game.api.constants.RedisConstant;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.RequestExpireInfo;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.exception.AuthException;
import com.wre.game.api.exception.RedisException;
import com.wre.game.api.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

@Component
public class SessionComponentImpl implements SessionComponent {

   
    private static final Logger logger = LoggerFactory.getLogger(SessionComponentImpl.class);

    @Value("${backend.api.token.secret}")
    private String tokenSecret;

    @Value("${backend.api.token.expire.millis}")
    private long tokenExpireMillis;

    @Value("${backend.api.token.redis.expire.second}")
    private int redisExpireSecond;

    @Resource
    @Qualifier("jedisPool")
    private JedisPool jedisPool;

    @Override
    public String createSessionAndToken(User user, String channel) {

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + tokenExpireMillis);
        String token = TokenUtil.generateToken(tokenSecret, user, expireTime, channel);

        Jedis jedis = null;
        try {
            String tokenKey = RedisConstant.PREFIX_KEY_TOKEN + token;
            String tokenValue = String.valueOf(user.getUserId());

            String userKey = RedisConstant.PREFIX_KEY_USER + user.getUserId();
            String userField = channel;
            String userValue = token;

            jedis = jedisPool.getResource();
            jedis.hset(tokenKey, ApiConstants.TOKEN_HASH_KEY_USER_ID, tokenValue);
            jedis.expire(tokenKey, redisExpireSecond);

            String oldTokenKey = jedis.hget(userKey, userField);
            if (oldTokenKey != null)
                jedis.del(RedisConstant.PREFIX_KEY_TOKEN + oldTokenKey);

            // if exist, overwrite
            jedis.hset(userKey, userField, userValue);
            jedis.expire(userKey, redisExpireSecond);

        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return token;
    }

    @Override
    public SessionDomain getSessionByChannel(String token, String channel) throws AuthException {
        return this.getSessionByChannel(token, channel, true);
    }

    @Override
    public SessionDomain getSessionByChannel(String token, String channel, boolean checkExpired) throws AuthException {

        SessionDomain sessionDomain = null;
        logger.info("--------"+channel+token);
        try {
            if(StringUtils.isBlank(token)){
                throw new AuthException(RtCode.RT_TOKEN_INVALID);
            }
            sessionDomain = TokenUtil.parseToken(tokenSecret, token);
            logger.info("session:{}", sessionDomain);
            if (!sessionDomain.getChannel().equals(channel)) {
                logger.info(sessionDomain.getChannel()+"_"+channel);
                throw new AuthException(RtCode.RT_TOKEN_INVALID);
            }
            // check redis
            Map<String, String> tokenValueMap = getUserIdByToken(token);
            if (tokenValueMap == null) {
                if (checkExpired) {
                    throw new AuthException(RtCode.RT_TOKEN_INVALID);
                } else {
                    sessionDomain = null;
                }
            } else if (!StringUtils.isBlank(tokenValueMap.get(ApiConstants.TOKEN_HASH_KEY_OLD))) {
                Boolean isOld = Boolean.valueOf(tokenValueMap.get(ApiConstants.TOKEN_HASH_KEY_OLD));
                if (isOld && checkExpired)
                    throw new AuthException(RtCode.RT_TOKEN_OLD);
            }
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
//            logger.warn("Token invalid", e);
//            e.printStackTrace();
            logger.error(e.getMessage(),e);
            throw new AuthException(RtCode.RT_TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            logger.error(e.getMessage(),e);
            // checkExpired==false  sessionDomain == null.
            if (checkExpired) {
                if (getUserIdByToken(token) == null)
                    throw new AuthException(RtCode.RT_TOKEN_INVALID);
                else
                    throw new AuthException(RtCode.RT_TOKEN_EXPIRED);
            }
        }
        return sessionDomain;
    }

    @Override
    public Long getUserIdByChannel(String token, String channel) throws AuthException {
        Long userId = null;
        try {
                if(!StringUtils.isBlank(token)){
                    SessionDomain sessionDomain = TokenUtil.parseToken(tokenSecret, token);
                    userId = Long.valueOf(sessionDomain.getUserId());
                }

        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException | ExpiredJwtException e) {
            logger.error("Token invalid, try to get userId from redis", e);
            try {
                Map<String, String> tokenValueMap = getUserIdByToken(token);
                if (tokenValueMap != null) {
                    userId = Long.valueOf(tokenValueMap.get(ApiConstants.TOKEN_HASH_KEY_USER_ID));
                }
            } catch (Exception e1) {
                logger.error("can not get userId from redis:{}", token);
              logger.error(e1.getMessage(),e1);
            }
        }

        if (userId == null) {
            logger.error("can not get userId by channel: {} and token : {}", channel, token);
            throw new AuthException(RtCode.RT_TOKEN_INVALID);
        }

        return userId;
    }

    @Override
    public Map<String, String> getUserIdByToken(String token) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Map<String, String> tokenValueMap = jedis.hgetAll(RedisConstant.PREFIX_KEY_TOKEN + token);
            //差异性
            if (tokenValueMap == null || tokenValueMap.isEmpty()) {
                return null;
            }else {
                return tokenValueMap;
            }
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR, e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void kickoutSessionAll(Long uid) {

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            String userKey = RedisConstant.PREFIX_KEY_USER + uid;
            Map<String, String> map = jedis.hgetAll(userKey);
            if (map == null)
                return;

            for (String token : map.values()) {
                jedis.del(RedisConstant.PREFIX_KEY_TOKEN + token);
            }
            jedis.del(userKey);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void kickoutSessionByChannel(Long uid, String channel) {

        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            String userKey = RedisConstant.PREFIX_KEY_USER + uid;
            String token = jedis.hget(userKey, channel);
            if (token == null)
                return;

            jedis.del(RedisConstant.PREFIX_KEY_TOKEN + token);
            jedis.hdel(userKey, channel);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void kickoutSessionByChannel(String token, String channel) {
        Jedis jedis = null;

        try {
            jedis = jedisPool.getResource();
            String tokenKey = RedisConstant.PREFIX_KEY_TOKEN + token;
            Map<String, String> tokenValueMap = jedis.hgetAll(tokenKey);
            if (tokenValueMap == null || tokenValueMap.isEmpty())
                return;

            jedis.del(tokenKey);
            jedis.hdel(RedisConstant.PREFIX_KEY_USER + tokenValueMap.get(ApiConstants.TOKEN_HASH_KEY_USER_ID), channel);
        } catch (Exception e) {
            logger.info("Jedis Operation Error", e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public void kickoutSessionOtherChannel(Long uid, String channel) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String userKey = RedisConstant.PREFIX_KEY_USER + uid;
            Map<String, String> map = jedis.hgetAll(userKey);
            if (map == null)
                return;

            for (String channelKey : map.keySet()) {
                if (StringUtils.equals(channel, channelKey)) {
                    String survivingToken = map.get(channelKey);
                    boolean isTokenExist = jedis.exists(RedisConstant.PREFIX_KEY_TOKEN + survivingToken);
                    if (isTokenExist)
                        jedis.hset(RedisConstant.PREFIX_KEY_TOKEN + survivingToken, ApiConstants.TOKEN_HASH_KEY_OLD, Boolean.TRUE.toString());
                } else {
                    String kickoutToken = map.get(channelKey);
                    jedis.del(RedisConstant.PREFIX_KEY_TOKEN + kickoutToken);
                    jedis.hdel(userKey, channelKey);
                }
            }
        } catch (Exception e) {
            logger.info("Jedis Operation Error"+e.getMessage(), e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }

    @Override
    public boolean getRequestExprie(RequestExpireInfo requestExpireInfo, String token) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            boolean exists = jedis.exists(requestExpireInfo.getPath()+token);
            if (exists){
                return false;
            }
            jedis.set(requestExpireInfo.getPath()+token, requestExpireInfo.getPath());
            jedis.expire(requestExpireInfo.getPath()+token,requestExpireInfo.getExpire());
            return true;
        } catch (Exception e) {
            logger.info("Jedis Operation Error"+e.getMessage(), e);
            throw new RedisException(RtCode.RT_INTERNAL_ERROR);
        } finally {
            if (jedis != null)
                jedis.close();
        }
    }
}
