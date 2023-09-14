package com.wre.game.api.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.wre.game.api.adapter.BaiduAdapter;
import com.wre.game.api.adapter.QqAdapter;
import com.wre.game.api.adapter.ToutiaoAdapter;
import com.wre.game.api.adapter.WechatAdapter;
import com.wre.game.api.constants.*;
import com.wre.game.api.dao.*;
import com.wre.game.api.data.*;
import com.wre.game.api.data.entity.*;
import com.wre.game.api.data.param.FBInvitedReqParam;
import com.wre.game.api.data.param.InvitedReqParam;
import com.wre.game.api.data.param.UserLoginReqParam;
import com.wre.game.api.exception.QqException;
import com.wre.game.api.exception.WechatException;
import com.wre.game.api.message.WechatMessage;
import com.wre.game.api.service.UserDataStorageService;
import com.wre.game.api.util.Fn;
import com.wre.game.api.util.RandomUtil4in;
import com.wre.game.api.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.data.dto.UserLoginDTO;
import com.wre.game.api.data.dto.UserShareFromDTO;
import com.wre.game.api.exception.ApiException;
import com.wre.game.api.exception.AuthException;
import com.wre.game.api.service.UserService;
import com.wre.game.api.util.TokenUtil;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Resource
    private UserDao userDao;
    @Resource
    private UserChannelMapper userChannelDao;

    @Resource
    private UserLoginHistoryDao userLoginHistoryDao;

    @Resource
    private SessionComponent sessionComponent;
    @Resource
    private UserGemMapper userGemMapper;
    @Resource
    private WechatAdapter wechatAdapter;
    @Resource
    private BaiduAdapter baiduAdapter;
    @Resource
    private UserService userService;
    @Resource
    private QqAdapter qqAdapter;
    @Resource
    private ToutiaoAdapter toutiaoAdapter;
    @Resource
    private UserDataStorageService userDataStorageService;
    @Resource
    AbTestInfoMapper abTestInfoMapper;

    @Value("${backend.api.token.secret}")
    private String tokenSecret;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserLoginDTO executeLogin(User user) {
        logger.debug("user login info :{}", user);
        Date now = new Date();
        //1. check existed user or not.
        User existUser = userDao.selectUserByOpenIdAndAppId(user.getOpenId(), user.getAppId());
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        String token = null;
        String uuid = "";
        if (existUser == null) {
            // save user info in db
            uuid = TokenUtil.generateUuid();
            user.setUuid(uuid);
            user.setCreatedAt(now);
           // user.setGroupType(userService.AbTest(user));
            userDao.insertUser(user);
            token = sessionComponent.createSessionAndToken(user, user.getChannel());
            userLoginDTO.setUserId(user.getUserId());
            userLoginDTO.setOpenId(user.getOpenId());
            userLoginDTO.setUuid(user.getUuid());
            userLoginDTO.setBlock(UserStatus.ACTIVE.value());
            userLoginDTO.setToken(token);
            userLoginDTO.setAvatarUrl("");
            userLoginDTO.setNickName("");
           // userLoginDTO.setGroupType(user.getGroupType());
        } else {
            uuid = existUser.getUuid();
            token = sessionComponent.createSessionAndToken(existUser, existUser.getChannel());
           // user.setGroupType(existUser.getGroupType());
            //userDao.updateUserByGroupType(user);
            userLoginDTO.setUserId(existUser.getUserId());
            userLoginDTO.setOpenId(existUser.getOpenId());
            userLoginDTO.setUuid(uuid);
            userLoginDTO.setBlock(existUser.getBlock());
            userLoginDTO.setToken(token);
            userLoginDTO.setAvatarUrl(existUser.getAvatarUrl());
            userLoginDTO.setNickName(existUser.getNickName());
           // userLoginDTO.setGroupType(user.getGroupType());
        }

        //2. insert user share info.

        if (StringUtils.isNumeric(user.getShareType()) && Integer.valueOf(user.getShareType()) >= 10) {
            UserShareFrom usf = userDao.selectShareFromFunctional(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId());

            logger.info("user share info functional : {} ", usf);
            if (usf == null || !DateUtils.isSameDay(new Date(), usf.getCreatedAt())) {
                userDao.insertUserShareFromFunctional(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId(), user.getAppName());
            } else {
                logger.info("user share info functional already existed. fromUuid : {} , toUuid : {}", user.getShareFromUuid(), uuid);
            }

        } else {
            Integer i = userDao.selectExistShareFrom(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId());
            if (i == 0) {
                userDao.insertUserShareFrom(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId(), user.getAppName());
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("user share info already existed. fromUuid : {} , toUuid : {}", user.getShareFromUuid(), uuid);
                }
            }
        }

        //3. insert user login history
        UserLoginHistory userLoginHistory = new UserLoginHistory();
        userLoginHistory.setLoginTime(new Date());
        userLoginHistory.setChannel(user.getChannel());
        userLoginHistory.setAppVersion(user.getAppVersion());
        userLoginHistory.setAppName(user.getAppName());
        userLoginHistory.setAppId(user.getAppId());
        userLoginHistory.setLoginTime(now);
        //userLoginHistory.setGroupType(user.getGroupType());
        if (existUser == null) {
            userLoginHistory.setNewUserYn("y");
            userLoginHistory.setUuid(user.getUuid());
            userLoginHistory.setUserId(user.getUserId());
            userLoginHistory.setUserCreatedAt(user.getCreatedAt());
        } else {
            userLoginHistory.setNewUserYn("n");
            userLoginHistory.setUuid(existUser.getUuid());
            userLoginHistory.setUserId(existUser.getUserId());
            userLoginHistory.setUserCreatedAt(existUser.getCreatedAt());
        }
        userLoginHistoryDao.insertUserLoginHistory(userLoginHistory);

        userLoginDTO.setSessionKey(user.getSessionKey());

//        boolean legal= GameUtils.compareVersion();
        GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(user.getAppId());
        if(gameInfo!=null){
             if (StringUtils.equals(gameInfo.getServerVersion(), user.getAppVersion())) {
                 userLoginDTO.setLegal(true);
             } else {
                 userLoginDTO.setLegal(false);
             }
        }else{
            userLoginDTO.setLegal(false);
        }


        return userLoginDTO;
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public UserLoginDTO executeLoginByChannel(User user) {
        logger.debug("user login info :{}", user);
        Date now = new Date();
        //1. check existed user or not.
        User existUser = userDao.selectUserByChannelIdAndAppId(user.getChannelId(), user.getAppId(),user.getChannelName());
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        String token = null;
        String uuid = "";
        if (existUser == null) {
            /**查看以前是否有账号 如果有根据以前账号绑定渠道*/
            existUser=userDao.selectUserByOpenIdAndAppId(user.getOpenId(),user.getAppId());
            UserChannel userChannel=new UserChannel();
            /**
             * 如果有该用户,且该用户已经绑定了账号 也生成一个新的账号给与玩家
             * 一个渠道一个号只能绑定一个账号
             * */
            if(existUser!=null){
                userChannel=userChannelDao.selectByUUID(existUser.getUuid(),user.getChannelName());
            }
            if(existUser==null||userChannel!=null) {
                // save user info in db
                uuid = TokenUtil.generateUuid();
                user.setUuid(uuid);
                user.setCreatedAt(now);
                userDao.insertUser(user);
                userLoginDTO.setAvatarUrl("");
                userLoginDTO.setNickName("");
                userLoginDTO.setUserId(user.getUserId());
                userLoginDTO.setOpenId(user.getOpenId());
                userLoginDTO.setBlock(UserStatus.ACTIVE.value());
            }else {
                uuid= existUser.getUuid();
                userLoginDTO.setAvatarUrl(existUser.getUuid());
                userLoginDTO.setAvatarUrl(existUser.getAvatarUrl());
                userLoginDTO.setNickName(existUser.getNickName());
                userLoginDTO.setUserId(existUser.getUserId());
                userLoginDTO.setOpenId(existUser.getOpenId());
                userLoginDTO.setBlock(existUser.getBlock());
            }
            userChannel=new UserChannel();
            userChannel.setUserAppId(user.getAppId());
            userChannel.setUserChannel(user.getChannel());
            userChannel.setUserChannelId(user.getChannelName());
            userChannel.setUserUuid(uuid);
            userChannelDao.insertSelective(userChannel);
            token = sessionComponent.createSessionAndToken(user, user.getChannel());
            userLoginDTO.setUuid(uuid);
            userLoginDTO.setToken(token);
        } else {
            uuid = existUser.getUuid();
            token = sessionComponent.createSessionAndToken(existUser, existUser.getChannel());
            userLoginDTO.setUuid(uuid);
            userLoginDTO.setToken(token);
            userLoginDTO.setAvatarUrl(existUser.getAvatarUrl());
            userLoginDTO.setNickName(existUser.getNickName());
            userLoginDTO.setUserId(existUser.getUserId());
            userLoginDTO.setOpenId(existUser.getOpenId());
            userLoginDTO.setBlock(existUser.getBlock());
        }

        //2. insert user share info.

        if (StringUtils.isNumeric(user.getShareType()) && Integer.valueOf(user.getShareType()) >= 10) {
            UserShareFrom usf = userDao.selectShareFromFunctional(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId());

            logger.info("user share info functional : {} ", usf);
            if (usf == null || !DateUtils.isSameDay(new Date(), usf.getCreatedAt())) {
                userDao.insertUserShareFromFunctional(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId(), user.getAppName());
            } else {
                logger.info("user share info functional already existed. fromUuid : {} , toUuid : {}", user.getShareFromUuid(), uuid);
            }

        } else {
            Integer i = userDao.selectExistShareFrom(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId());
            if (i == 0) {
                userDao.insertUserShareFrom(user.getShareFromUuid(), uuid, user.getShareType(), user.getAppId(), user.getAppName());
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("user share info already existed. fromUuid : {} , toUuid : {}", user.getShareFromUuid(), uuid);
                }
            }
        }

        //3. insert user login history
        UserLoginHistory userLoginHistory = new UserLoginHistory();

        userLoginHistory.setLoginTime(new Date());
        userLoginHistory.setChannel(user.getChannel());
        userLoginHistory.setAppVersion(user.getAppVersion());
        userLoginHistory.setAppName(user.getAppName());
        userLoginHistory.setAppId(user.getAppId());
        userLoginHistory.setLoginTime(now);
        if (existUser == null) {
            userLoginHistory.setNewUserYn("y");
            userLoginHistory.setUuid(user.getUuid());
            userLoginHistory.setUserId(user.getUserId());
            userLoginHistory.setUserCreatedAt(user.getCreatedAt());
        } else {
            userLoginHistory.setNewUserYn("n");
            userLoginHistory.setUuid(existUser.getUuid());
            userLoginHistory.setUserId(existUser.getUserId());
            userLoginHistory.setUserCreatedAt(existUser.getCreatedAt());
        }
        userLoginHistoryDao.insertUserLoginHistory(userLoginHistory);

        userLoginDTO.setSessionKey(user.getSessionKey());

//        boolean legal= GameUtils.compareVersion();
        GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(user.getAppId());
        if(gameInfo!=null){
            if (StringUtils.equals(gameInfo.getServerVersion(), user.getAppVersion())) {
                userLoginDTO.setLegal(true);
            } else {
                userLoginDTO.setLegal(false);
            }
        }else{
            userLoginDTO.setLegal(false);
        }

        return userLoginDTO;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public RtCode executeEdit(User user) {
        int i = userDao.updateUserById(user);
        if (i > 0) {
            return RtCode.RT_SUCCESS;
        } else {
            throw new ApiException(RtCode.RT_INTERNAL_ERROR);
        }
    }

    @Override
    public UserLoginDTO executeExtendToken(String channel, String token, String appId, String version, User share) {


        Long userId = sessionComponent.getUserIdByChannel(token, channel);

        // STEP 01. Get User Object;
        User user = userDao.selectUserById(userId);

        if (user == null) {
            logger.error("request user not found userId:{}", userId);
            throw new AuthException(RtCode.RT_LOGIN_USER_NOT_FOUND);
        }
        if (user.getBlock().equals(UserStatus.BLOCK.value())) {
            logger.error("request user blocked:{}", user);
            throw new AuthException(RtCode.RT_LOGIN_USER_BLOCKED);
        }

        // STEP 02. ReIssue Token
        sessionComponent.kickoutSessionByChannel(userId, channel);
        token = sessionComponent.createSessionAndToken(user, channel);

        // Step 03. LastLoginTime update.
        userDao.updateLastLoginTimeById(user.getUserId());

        // Step 04. Historical issue
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserId(userId);
        userLoginDTO.setToken(token);
        userLoginDTO.setNickName(user.getNickName());
        userLoginDTO.setAvatarUrl(user.getAvatarUrl());
        userLoginDTO.setOpenId(user.getOpenId());
        userLoginDTO.setBlock(user.getBlock());

        GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(appId);
        if (StringUtils.equals(gameInfo.getServerVersion(), version)) {
            userLoginDTO.setLegal(true);
        } else {
            userLoginDTO.setLegal(false);
        }

        // Step 05. insert share information
        String shareType = share.getShareType();
        String fromUuid = share.getShareFromUuid();
        String uuid = user.getUuid();
        if (StringUtils.isNumeric(shareType) && Integer.valueOf(shareType) >= 10) {
            UserShareFrom usf = userDao.selectShareFromFunctional(fromUuid, uuid, shareType, appId);
            logger.info("user share info functional : {} ", usf);
            if (usf == null || !DateUtils.isSameDay(new Date(), usf.getCreatedAt())) {
                userDao.insertUserShareFromFunctional(fromUuid, uuid, shareType, appId, share.getAppName());
            } else {
                logger.info("user share info functional already existed. fromUuid : {} , toUuid : {}", user.getShareFromUuid(), uuid);
            }
        }
        return userLoginDTO;

    }

    @Override
    public List<UserShareFromDTO> getShareStatistics(String uuid, String shareType, String appId) {
        return userDao.selectShareStatistics(uuid, shareType, appId, null, null);
    }

    @Override
//    @Cacheable(value = "user_share", keyGenerator = "springCacheKeyGenerator")
    public List<UserShareFromDTO> getShareStatisticsFunctionToday(String uuid, String shareType, String appId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startTime = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date endTime = calendar.getTime();
        return userDao.selectShareStatisticsFunctional(uuid, shareType, appId, startTime, endTime);
    }

    @Override
    public UserLoginDTO getUserLoginByWechat(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock) {
        try {
            WechatSession wechatSession = wechatAdapter.codeToSession(body.getCode(), logingRepUser.getAppId());
            if("wxee875f50f2bb55b4".equals(logingRepUser.getAppId())) {
                logger.warn("微信的传入:-code:" + body.getCode() + "-appId:" + logingRepUser.getAppId());
                logger.warn("微信的返回:"+wechatSession.toString());
                //code值是否为空
            }
            String openId = wechatSession.getOpenId();
            String unionId = wechatSession.getUnionId();
            String sessionKey = wechatSession.getSessionKey();
            UserLoginDTO userLoginDTO=new UserLoginDTO();
            /**
             * 2020年10月10日09:44:20 由于errorcode没有赋值导致出现很多问题
             * 这里判断如果有openId默认给errorcode赋值
             * */
            if(!StringUtils.isBlank(wechatSession.getOpenId())){
                wechatSession.setErrCode(WechatMessage.WechatInto.ERROR_CODE_SUC);
            }
            if (wechatSession.getErrCode() != null) {
                switch (wechatSession.getErrCode()) {
                    case 0:
                        User user = new User();
                        user.setShareFromUuid(logingRepUser.getShareId());
                        user.setOpenId(openId);
                        user.setAppId(logingRepUser.getAppId());
                        user.setAppName(logingRepUser.getAppName());
                        user.setShareType(body.getShareType());
                        user.setUnionId(unionId);
                        user.setSessionKey(sessionKey);
                        user.setAppVersion(logingRepUser.getVersion());
                        user.setBlock(0);
                        user.setChannel(logingRepUser.getChannel());
                        user.setSceneId(body.getSceneId());
                        user.setShareFromImage(body.getShareFromImage());
                         userLoginDTO = userService.executeLogin(user);
                        userLoginDTO.setIpBlock(ipBlock);
                        return userLoginDTO;
                    case -1:
                    case 40029:
                    case 45011:
                    default:
                        logger.error("login failed --> " + wechatSession);
                        logger.error("logingRepUser--->"+logingRepUser);
                        logger.error("login param--->" + body);
                        throw new WechatException(RtCode.RT_LOGIN_WECHAT_ERROR, wechatSession.getErrMsg());
                }
            }
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public UserLoginDTO getUserLoginByDouyin(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock) {
        try {
            ToutiaoSession toutiaoSession = toutiaoAdapter.codeToSession(body.getCode(), body.getAnonymousCode(), logingRepUser.getAppId());
            String openId = toutiaoSession.getOpenId();
            String anonymousOpenId = toutiaoSession.getAnonymousOpenId();
            if (StringUtils.isBlank(openId)) {
                // 如果头条openId为空，使用anonymousOpenId替代openId创建账户
                openId = anonymousOpenId;
            }
            String sessionKey = toutiaoSession.getSessionKey();

            if (toutiaoSession.getError() != null && toutiaoSession.getError() > 0) {
                logger.error("login failed --> " + toutiaoSession);
                logger.error("logingRepUser--->"+logingRepUser);
                logger.error("login param:" + body);
                throw new WechatException(RtCode.RT_LOGIN_TOUTIAO_ERROR, toutiaoSession.getMessage());
            }else {
                User user = new User();
                user.setShareFromUuid(logingRepUser.getShareId());
                user.setOpenId(openId);
                user.setAnonymousOpenId(anonymousOpenId);
                user.setAppId(logingRepUser.getAppId());
                user.setAppName(logingRepUser.getAppName());
                user.setShareType(body.getShareType());
                user.setUnionId(null);
                user.setSessionKey(sessionKey);
                user.setAppVersion(logingRepUser.getVersion());
                user.setBlock(0);
                user.setChannel(logingRepUser.getChannel());
                user.setSceneId(body.getSceneId());
                user.setShareFromImage(body.getShareFromImage());
                UserLoginDTO userLoginDTO = userService.executeLogin(user);
                userLoginDTO.setIpBlock(ipBlock);
                return userLoginDTO;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }

    }

    @Override
    public UserLoginDTO getUserLoginByToutiao(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock) {
        /**
         * 这里头条和抖音是一个代码逻辑没有分开
         * 为了以后出现差异性分开
         * 现在分为两个代码
         * 重复代码没有办法
         * 2020年8月20日21:20:28 申欣武
         */
        try {
            ToutiaoSession toutiaoSession = toutiaoAdapter.codeToSession(body.getCode(), body.getAnonymousCode(), logingRepUser.getAppId());
            String openId = toutiaoSession.getOpenId();
            String anonymousOpenId = toutiaoSession.getAnonymousOpenId();
            if (StringUtils.isBlank(openId)) {
                // 如果头条openId为空，使用anonymousOpenId替代openId创建账户
                openId = anonymousOpenId;
            }
            String sessionKey = toutiaoSession.getSessionKey();

            if (toutiaoSession.getError() != null && toutiaoSession.getError() > 0) {
                logger.error("login failed --> " + toutiaoSession);
                logger.error("logingRepUser--->"+logingRepUser);
                logger.error("login param:" + body);
                throw new WechatException(RtCode.RT_LOGIN_TOUTIAO_ERROR, toutiaoSession.getMessage());
            }else {
                User user = new User();
                user.setShareFromUuid(logingRepUser.getShareId());
                user.setOpenId(openId);
                user.setAnonymousOpenId(anonymousOpenId);
                user.setAppId(logingRepUser.getAppId());
                user.setAppName(logingRepUser.getAppName());
                user.setShareType(body.getShareType());
                user.setUnionId(null);
                user.setSessionKey(sessionKey);
                user.setAppVersion(logingRepUser.getVersion());
                user.setBlock(0);
                user.setChannel(logingRepUser.getChannel());
                user.setSceneId(body.getSceneId());
                user.setShareFromImage(body.getShareFromImage());
                UserLoginDTO userLoginDTO = userService.executeLogin(user);
                userLoginDTO.setIpBlock(ipBlock);
                return userLoginDTO;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public UserLoginDTO getUserLoginByQqmini(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock) {
        try {
            QqSession qqSession = qqAdapter.codeToSession(body.getCode(), logingRepUser.getAppId());
            String openId = qqSession.getOpenId();
            String unionId = qqSession.getUnionId();
            String sessionKey = qqSession.getSessionKey();
            if (qqSession.getErrCode() != null) {
                switch (qqSession.getErrCode()) {
                    case 0:
                        User user = new User();
                        user.setShareFromUuid(logingRepUser.getShareId());
                        user.setOpenId(openId);
                        user.setAppId(logingRepUser.getAppId());
                        user.setAppName(logingRepUser.getAppName());
                        user.setShareType(body.getShareType());
                        user.setUnionId(unionId);
                        user.setSessionKey(sessionKey);
                        user.setAppVersion(logingRepUser.getVersion());
                        user.setBlock(0);
                        user.setChannel(logingRepUser.getChannel());
                        user.setSceneId(body.getSceneId());
                        user.setShareFromImage(body.getShareFromImage());
                        UserLoginDTO userLoginDTO = userService.executeLogin(user);
                        userLoginDTO.setIpBlock(ipBlock);
                        return userLoginDTO;
                    case -1:
                    case 40029:
                    case 45011:
                    default:
                        logger.error("login failed --> " + qqSession);
                        logger.error("logingRepUser--->"+logingRepUser);
                        logger.error("login param--->" + body);
                        throw new QqException(RtCode.RT_LOGIN_QQ_ERROR, qqSession.getErrMsg());
                }
            }
            return null;
        } catch (QqException e) {
            logger.error(e.getMessage(),e);
            return null;
        }

    }

    @Override
    public UserLoginDTO getUserLoginBySwan(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock) {
        try {
            BaiduSession baiduSession = baiduAdapter.codeToSession(body.getCode(), logingRepUser.getAppId());
            String openId =baiduSession.getOpenid();
            String sessionKey = baiduSession.getSession_key();
            if (baiduSession.getError() == null) {
                User user = new User();
                user.setShareFromUuid(logingRepUser.getShareId());
                user.setOpenId(openId);
                user.setAppId(logingRepUser.getAppId());
                user.setAppName(logingRepUser.getAppName());
                user.setShareType(body.getShareType());
                user.setUnionId(null);
                user.setSessionKey(sessionKey);
                user.setAppVersion(logingRepUser.getVersion());
                user.setBlock(0);
                user.setChannel(logingRepUser.getChannel());
                user.setSceneId(body.getSceneId());
                user.setShareFromImage(body.getShareFromImage());
                UserLoginDTO userLoginDTO = userService.executeLogin(user);
                userLoginDTO.setIpBlock(ipBlock);
                return userLoginDTO;
            } else {
                logger.error("login failed --> " + baiduSession);
                logger.error("logingRepUser--->"+logingRepUser);
                logger.error("login param--->" + body);
                throw new WechatException(RtCode.RT_LOGIN_WECHAT_ERROR, baiduSession.getError_description());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public UserLoginDTO getUserLoginByOtherChannel(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock) {
        try {
            User user = new User();
            user.setShareFromUuid(logingRepUser.getShareId());
            user.setOpenId(SecureUtil.md5(body.getCode()));
            user.setAppId(logingRepUser.getAppId());
            user.setAppName(logingRepUser.getAppName());
            user.setShareType(body.getShareType());
            user.setUnionId(null);
            user.setSessionKey(null);
            user.setAppVersion(logingRepUser.getVersion());
            user.setBlock(0);
            user.setChannelId(logingRepUser.getChannelId());
            user.setChannel(logingRepUser.getChannel());
            user.setChannelName(body.getChannelName());
            user.setSceneId(body.getSceneId());
            user.setShareFromImage(body.getShareFromImage());

            UserLoginDTO userLoginDTO = userService.executeLoginByChannel(user);
            userLoginDTO.setIpBlock(ipBlock);
            return userLoginDTO;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }

    @Override
    public UserLoginDTO getUserLoginByDefault(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock,boolean isOpen4Code) {
        try {
            String openId=null;
            if(isOpen4Code){
                openId=body.getCode();
            }
            User user = new User();
            user.setShareFromUuid(logingRepUser.getShareId());
            user.setOpenId(openId);
            user.setAppId(logingRepUser.getAppId());
            user.setAppName(logingRepUser.getAppName());
            user.setShareType(body.getShareType());
            user.setUnionId(null);
            user.setSessionKey(null);
            user.setAppVersion(logingRepUser.getVersion());
            user.setBlock(0);
            user.setChannel(logingRepUser.getChannel());
            user.setSceneId(body.getSceneId());
            user.setShareFromImage(body.getShareFromImage());
            UserLoginDTO userLoginDTO = userService.executeLogin(user);
            userLoginDTO.setIpBlock(ipBlock);
            return userLoginDTO;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return null;
        }
    }


    @Override
    public String AbTest(User user) {
//        if(AbTestInfoStarted.sycABTestMap==null||AbTestInfoStarted.sycABTestMap.isEmpty()){
//            return RandomUtil4in.defaultGroup;
//        }
        AbTestInfo abTestInfo=null;
//        for(AbTestInfo ab:AbTestInfoStarted.sycABTestMap){
//            if(isStartTimeCode(ab)&&isEndTimeCode(ab)&&appId.equals(ab.getAppid())){
//                abTestInfo=ab;
//                break;
//            }
//        }
        if (StringUtils.isEmpty(user.getAppVersion())){
            return RandomUtil4in.defaultGroup;
        }
        abTestInfo=abTestInfoMapper.selectByAppVersion(user);
        if(abTestInfo==null){
            return RandomUtil4in.defaultGroup;
        }
       return RandomUtil4in.getRandom4ABTest(abTestInfo.getWeight());
    }

    @Override
    public int getInvitedFriends(String fromUuid, String shareType, String appId) {
        return userDao.getInvitedFriends(fromUuid,shareType,appId);
    }

    @Override
    public ResponseEntity<ApiResponseMessage> invitedFriends(InvitedReqParam invitedReqParam,String appId) {
        try{
            for (FBInvitedReqParam fbInvitedReqParam : invitedReqParam.getBody()) {
                UserShareFrom usf = userDao.selectShareFromFunctional(fbInvitedReqParam.getUserId(), invitedReqParam.getUuid(), fbInvitedReqParam.getSharePos(), appId);
                logger.info("user share info functional : {} ", usf);
                if (usf == null || !DateUtils.isSameDay(new Date(), usf.getCreatedAt())) {
                    userDao.insertUserShareFromFunctional(fbInvitedReqParam.getUserId(), invitedReqParam.getUuid(), fbInvitedReqParam.getSharePos(),appId, appId);
                } else {
                    logger.info("user share info functional already existed. fromUuid : {} , toUuid : {}", fbInvitedReqParam.getUserId(), invitedReqParam.getUuid());
                }
            }

            RtCode rtCode = RtCode.RT_SUCCESS;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            RtCode rtCode = RtCode.RT_INTERNAL_ERROR;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
    }

    @Override
    public boolean addUserGem(Long userId, String appId, Integer gem) {
        UserGem userGem = userGemMapper.getUserGem(userId, appId);
        int result = 0;
        if (userGem == null) {
            UserGem createGem = new UserGem();
            createGem.setUserId(userId);
            createGem.setAppId(appId);
            createGem.setGem(Long.valueOf(gem));
            result = userGemMapper.insertUserGem(createGem);
        } else {
            Long userGemNum = userGem.getGem();
            userGem.setGem(userGemNum + gem);
            result = userGemMapper.updateUserGem(userGem);
        }
        return result >= 1;
    }

    @Override
    public boolean plusUserGem(Long userId, String appId, Integer gem) {
        UserGem userGem = userGemMapper.getUserGem(userId, appId);
        if (userGem == null) {
            return false;
        }
        if (userGem.getGem() < gem) {
            userGem.setGem(0L);
        } else {
            userGem.setGem(userGem.getGem() - gem);
        }
        return userGemMapper.updateUserGem(userGem) >= 1;
    }

    @Override
    public UserGem getUserGem(Long userId, String appId) {
        return userGemMapper.getUserGem(userId, appId);
    }

    @Override
    public void removeUser(Long userId, String appId) {
        User user = userDao.selectUserById(userId);
        if (user != null) {
            userDao.deleteUserById(user.getUserId());
            userDataStorageService.removeData(user.getUuid(),appId);
        }
    }

    /**
     * 判断兑换码是否激活
     */
    public boolean isEndTimeCode(AbTestInfo code) {
        if (code.getEndTime()==null) {
            return true;
        }
        int now = TimeUtil.getNowTime();
        int codeEndTime = Fn.toInt(code.getEndTime().getTime()/1000L);
        return now <= codeEndTime;
    }

    /**
     * 判断兑换码是否激活
     * true 激活
     */
    public boolean isStartTimeCode(AbTestInfo code) {
        if (code.getStartTime()==null) {
            return true;
        }
        int now = TimeUtil.getNowTime();
        int codeStartTime = Fn.toInt(code.getStartTime().getTime()/1000L);
        return now >= codeStartTime;
    }
}
