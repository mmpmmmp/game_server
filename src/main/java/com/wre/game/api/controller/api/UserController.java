package com.wre.game.api.controller.api;

import com.wre.game.api.adapter.BaiduAdapter;
import com.wre.game.api.adapter.QqAdapter;
import com.wre.game.api.channel.ChannelLogin;
import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.config.IpConfig;
import com.wre.game.api.constants.*;
import com.wre.game.api.data.IpBlock;
import com.wre.game.api.data.IpInfos;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.dto.UserLoginDTO;
import com.wre.game.api.data.entity.LogingRepUser;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.data.param.UserInfoReqParam;
import com.wre.game.api.data.param.UserLoginReqParam;
import com.wre.game.api.exception.ParameterException;
import com.wre.game.api.exception.WechatException;
import com.wre.game.api.message.ChannelInfo;
import com.wre.game.api.service.UserService;
import com.wre.game.api.util.Fn;
import com.wre.game.api.util.IpUtil;
import com.wre.game.api.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @menu UserController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_USERS)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private BaiduAdapter baiduAdapter;
    @Resource
    private QqAdapter qqAdapter;
    @Resource
    private SessionComponent sessionComponent;
    @Resource
    private UserService userService;
    @Autowired
    private Map<String, ChannelLogin> channelLoginMap;

    /**
     * @description 登录
     */
    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    public UserLoginDTO executeLogin(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            HttpServletRequest request,
            @RequestBody UserLoginReqParam userLoginReqParam) {
        logger.info("userLoginReqParam: {} ", userLoginReqParam);
        logger.info("开始登录:"+userLoginReqParam.getCode());
        Date startDate=new Date();
        String code = userLoginReqParam.getCode();
        String shareFromOpenId = userLoginReqParam.getShareFromOpenId();
        String shareFromUuid = userLoginReqParam.getShareFromUuid();
        String shareId = StringUtils.isNotBlank(shareFromUuid) ? shareFromUuid : shareFromOpenId;
        // Parameter validation
        if (StringUtils.isBlank(code)) {
            logger.error("login failed --> code is blank. "+userLoginReqParam.toString());
            logger.error("login failed --> code is blank. "+userLoginReqParam.toString());
            logger.error("                 appId: " + appId + "  appName: " + appName + "  version: " + version + "  channel: " + channel);
            throw new ParameterException(RtCode.RT_PARAMETER_ERROR);
        }
        // check ip block
        IpInfos ipInfos = IpConfig.getIpInfos();
        String remoteIp = IpUtil.getRemoteIp(request);
        IpBlock ipBlock = IpUtil.checkIpBlock(remoteIp, ipInfos.getBlockIpList());
        UserLoginDTO userLoginDTO = null;


        if (ipBlock.isBlock()) {
        	logger.info("====> remote ip : {} , ipBlock:{}", remoteIp, ipBlock);
            userLoginDTO = new UserLoginDTO();
            userLoginDTO.setIpBlock(ipBlock);
            return userLoginDTO;
        }

        LogingRepUser logingRepUser=new LogingRepUser();
        logingRepUser.setAppId(appId);
        logingRepUser.setAppName(appName);
        logingRepUser.setChannel(channel);
        logingRepUser.setVersion(version);
        logingRepUser.setClientIpAddr(clientIpAddr);
        logingRepUser.setShareId(shareId);
        /**
         * 判断渠道参数有改为facebooke
         */
        if(!StringUtils.isBlank(userLoginReqParam.getChannelId())&&!StringUtils.isBlank(userLoginReqParam.getChannelName())){
            channel=ChannelInfo.ChannelCode.FACE_BOOK;
        }

//        ChannelLogin channelLogin = Optional.ofNullable(channelLoginMap.get(channel))
//                .orElse(channelLoginMap.get(ChannelInfo.ChannelCode.DEFAULT_CHANNEL));
//
//        userLoginDTO = channelLogin.login(logingRepUser, userLoginReqParam, ipBlock);

        /**
         * 2020年8月20日20:48:33 申欣武
         * 修改登录代码
         * 源代码太冗长
         * 改为switch case并且把其中的方法全部分开
         * 魔法值全部改成静态变量
         * 增加可阅读性
         */
        switch (channel){
            //用户选择wechat登录
            case ChannelInfo.ChannelCode.WE_CHAT:
                userLoginDTO= userService.getUserLoginByWechat(logingRepUser,userLoginReqParam,ipBlock);
                /**
                 * TODO 不知道为什么要这么写
                 * 按理说没有返回参数直接报错 或者给客户端错误返回值
                 * 这里没有获取到平台对应code值后没有报错 反而继续往下面赋值
                 * 2020年8月20日21:32:46 申欣武
                 * 在不动源代码的情况下我进行了判断null赋值操作 常理来说是否定的 备注下
                 */
                if(userLoginDTO==null){
                    userLoginDTO=userService.getUserLoginByDefault(logingRepUser,userLoginReqParam,ipBlock,false );
                }
                break;
            //用户选择抖音登录
            case ChannelInfo.ChannelCode.DOU_YIN:
                userLoginDTO= userService.getUserLoginByDouyin(logingRepUser,userLoginReqParam,ipBlock);
                break;
            case ChannelInfo.ChannelCode.TOU_TIAO:
                userLoginDTO= userService.getUserLoginByToutiao(logingRepUser,userLoginReqParam,ipBlock);
                break;
            case ChannelInfo.ChannelCode.QQ_MINI:
                /**
                 * TODO 不知道为什么要这么写
                 * 按理说没有返回参数直接报错 或者给客户端错误返回值
                 * 这里没有获取到平台对应code值后没有报错 反而继续往下面赋值
                 * 2020年8月20日21:32:46 申欣武
                 * 在不动源代码的情况下我进行了判断null赋值操作 常理来说是否定的 备注下
                 */
                userLoginDTO= userService.getUserLoginByToutiao(logingRepUser,userLoginReqParam,ipBlock);
                if(userLoginDTO==null){
                    userLoginDTO=userService.getUserLoginByDefault(logingRepUser,userLoginReqParam,ipBlock,false);
                }
                break;
            case ChannelInfo.ChannelCode.S_WAN:
                userLoginDTO=userService.getUserLoginBySwan(logingRepUser,userLoginReqParam,ipBlock);
                break;
            case ChannelInfo.ChannelCode.FACE_BOOK:
                if(!StringUtils.isBlank(userLoginReqParam.getChannelId())&&!StringUtils.isBlank(userLoginReqParam.getChannelName())){
                    logingRepUser.setChannelId(userLoginReqParam.getChannelId());
                    userLoginDTO=userService.getUserLoginByOtherChannel(logingRepUser,userLoginReqParam,ipBlock);
                }else{
                    userLoginDTO=userService.getUserLoginByDefault(logingRepUser,userLoginReqParam,ipBlock,true);
                }
                break;
            default:
                userLoginDTO=userService.getUserLoginByDefault(logingRepUser,userLoginReqParam,ipBlock,true);
                break;
        }
        /**添加登录时间*/
        userLoginDTO.setLoginTime(Fn.toString(TimeUtil.getTime(new Date())));
        //判断是否为空 不为空则返回
        if(userLoginDTO!=null){
            return userLoginDTO;
        }
        else{
            logger.error("logingRepUser--->"+logingRepUser);
            logger.error("login param--->" + userLoginReqParam);
            throw new WechatException(RtCode.RT_LOGIN_O_ERROR, "----->登录错误");
        }
    }

    /**
     * @description 修改用户
     */
    @RequestMapping(value = "", method = {RequestMethod.PUT})
    public ResponseEntity<ApiResponseMessage> executeEdit(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody UserInfoReqParam userInfoReqParam) {
        logger.info("RequestBody: {} ", userInfoReqParam);
        // Parameter validation
        if (StringUtils.isBlank(userInfoReqParam.getNickName()) || StringUtils.isBlank(userInfoReqParam.getAvatarUrl())) {
            throw new ParameterException(RtCode.RT_PARAMETER_ERROR_NICKNAME_AVATAR);
        }
        if(StringUtils.isBlank(token)){
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel);
        Long userId = Long.parseLong(sessionDomain.getUserId());
        logger.debug(userInfoReqParam.toString());


        User user = new User();
        user.setUserId(userId);
        user.setNickName(userInfoReqParam.getNickName());
        user.setAvatarUrl(userInfoReqParam.getAvatarUrl());
        user.setCity(userInfoReqParam.getCity());
        user.setCountry(userInfoReqParam.getCountry());
        user.setProvince(userInfoReqParam.getProvince());
        user.setGender(userInfoReqParam.getGender());

        RtCode rtCode = userService.executeEdit(user);
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
    }

    /**
     * @description 刷新登录token
     */
    @RequestMapping(value = "/login", method = {RequestMethod.PUT})
    public UserLoginDTO executeExtendToken(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody UserLoginReqParam userLoginReqParam) {

        String shareFromOpenId = userLoginReqParam.getShareFromOpenId();
        String shareFromUuid = userLoginReqParam.getShareFromUuid();
        String shareFromImage = userLoginReqParam.getShareFromImage();
        String sceneId = userLoginReqParam.getSceneId();
        String shareType = userLoginReqParam.getShareType();
        String shareId = StringUtils.isNotBlank(shareFromUuid) ? shareFromUuid : shareFromOpenId;

        User user = new User();

        user.setShareFromUuid(shareId);
        user.setAppId(appId);
        user.setAppName(appName);
        user.setShareType(shareType);
        user.setAppVersion(version);
        user.setChannel(channel);
        user.setSceneId(sceneId);
        user.setShareFromImage(shareFromImage);

        return userService.executeExtendToken(channel, token, appId, version, user);

    }

    /**
     * @description 刷新登录token
     */
    @RequestMapping(value = "/remove", method = {RequestMethod.POST})
    public ResponseEntity<Object> removeUser(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token) {

        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel);
        Long userId = Long.parseLong(sessionDomain.getUserId());
        userService.removeUser(userId, appId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @description 刷新登录token
     */
    @RequestMapping(value = "/untie", method = {RequestMethod.POST})
    public ResponseEntity<Object> untieUser() {

//        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel);
//        Long userId = Long.parseLong(sessionDomain.getUserId());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
