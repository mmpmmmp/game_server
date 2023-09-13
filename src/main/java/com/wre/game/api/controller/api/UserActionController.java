package com.wre.game.api.controller.api;

import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.constants.*;
import com.wre.game.api.data.entity.UserActionAd;
import com.wre.game.api.data.entity.UserActionGame;
import com.wre.game.api.data.entity.UserActionShare;
import com.wre.game.api.data.param.UserActionAdReqParam;
import com.wre.game.api.data.param.UserActionGameReqParam;
import com.wre.game.api.data.param.UserActionShareReqParam;
import com.wre.game.api.service.UserActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserActionController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_USERS_ACTIONS)
public class UserActionController {

    private static final Logger logger = LoggerFactory.getLogger(UserActionController.class);
    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private UserActionService userActionService;

    /**
     * 广告
     * @param channel
     * @param reqParam
     * @return
     */
    @RequestMapping(value = "/ad", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> adActions(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,

            @RequestBody UserActionAdReqParam reqParam) {
        logger.info("RequestBody: {} ", reqParam);
        Long userId = sessionComponent.getUserIdByChannel(token, channel);

        UserActionAd action = new UserActionAd();
        action.setChannel(channel);
        action.setAppName(appName);
        action.setAppId(appId);
        action.setAdPosition(reqParam.getAdPosition());
        action.setAdType(reqParam.getAdType());
        action.setUserId(userId);

        RtCode rtCode = userActionService.saveUserActionAd(action);

        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
    }


    /**
     * 游戏
     * @param channel
     * @param reqParam
     * @return
     */
    @RequestMapping(value = "/game", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> gameActions(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,

            @RequestBody UserActionGameReqParam reqParam) {
        logger.info("RequestBody: {} ", reqParam);

        Long userId = sessionComponent.getUserIdByChannel(token, channel);

        UserActionGame action = new UserActionGame();
        action.setUserId(userId);
        action.setFromAppId(appId);
        action.setFromAppName(appName);
        action.setChannel(channel);
        action.setToAppId(reqParam.getToAppId());
        action.setToAppName(reqParam.getToAppName());


        RtCode rtCode = userActionService.saveUserActionGame(action);
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
    }


    /**
     * 分享
     * @param channel
     * @param reqParam
     * @return
     */
    @RequestMapping(value = "/share", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> shareActions(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,

            @RequestBody UserActionShareReqParam reqParam) {
        logger.info("RequestBody: {} ", reqParam);

        Long userId = sessionComponent.getUserIdByChannel(token, channel);

        UserActionShare action = new UserActionShare();
        action.setUserId(userId);
        action.setShareType(reqParam.getShareType());
        action.setChannel(channel);
        action.setAppId(appId);
        action.setAppName(appName);
        action.setImageId(reqParam.getImageId());

        RtCode rtCode = userActionService.saveUserActionShare(action);
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
    }

}
