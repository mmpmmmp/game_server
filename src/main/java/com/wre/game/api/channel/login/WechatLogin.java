package com.wre.game.api.channel.login;

import com.wre.game.api.adapter.WechatAdapter;
import com.wre.game.api.channel.ChannelLogin;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.IpBlock;
import com.wre.game.api.data.WechatSession;
import com.wre.game.api.data.dto.UserLoginDTO;
import com.wre.game.api.data.entity.LogingRepUser;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.data.param.UserLoginReqParam;
import com.wre.game.api.exception.WechatException;
import com.wre.game.api.message.ChannelInfo;
import com.wre.game.api.message.WechatMessage;
import com.wre.game.api.service.UserService;
import com.wre.game.api.service.impl.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service(ChannelInfo.ChannelCode.WE_CHAT)
public class WechatLogin implements ChannelLogin {

    @Resource
    private WechatAdapter wechatAdapter;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserLoginDTO login(LogingRepUser logingRepUser, UserLoginReqParam body, IpBlock ipBlock) {
        try {
            WechatSession wechatSession = wechatAdapter.codeToSession(body.getCode(), logingRepUser.getAppId());
            if ("wxee875f50f2bb55b4".equals(logingRepUser.getAppId())) {
                logger.warn("微信的传入:-code:" + body.getCode() + "-appId:" + logingRepUser.getAppId());
                logger.warn("微信的返回:" + wechatSession.toString());
                //code值是否为空
            }
            String openId = wechatSession.getOpenId();
            String unionId = wechatSession.getUnionId();
            String sessionKey = wechatSession.getSessionKey();
            UserLoginDTO userLoginDTO = new UserLoginDTO();
            /**
             * 2020年10月10日09:44:20 由于errorcode没有赋值导致出现很多问题
             * 这里判断如果有openId默认给errorcode赋值
             * */
            if (!StringUtils.isBlank(wechatSession.getOpenId())) {
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
                        logger.error("logingRepUser--->" + logingRepUser);
                        logger.error("login param--->" + body);
                        throw new WechatException(RtCode.RT_LOGIN_WECHAT_ERROR, wechatSession.getErrMsg());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return userService.getUserLoginByDefault(logingRepUser, body, ipBlock, false);
    }
}
