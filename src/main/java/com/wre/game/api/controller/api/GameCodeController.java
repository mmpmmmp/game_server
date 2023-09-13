package com.wre.game.api.controller.api;

import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.constants.*;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.param.GameCodeReqParam;
import com.wre.game.api.exception.ApiException;
import com.wre.game.api.service.GameCodeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * GameCodeController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_GAMECODE)
public class GameCodeController {


    private static final Logger logger = LoggerFactory.getLogger(GameCodeController.class);
    @Resource
    private SessionComponent sessionComponent;
    @Resource
    private GameCodeService gameCodeService;
    /**
     *
     * 获取奖励
     * @return
     */
    @RequestMapping(value = "/getAward", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage>getAward(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                        @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                        @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                        @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                        @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                        @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                        @RequestBody GameCodeReqParam body) {
        if(StringUtils.isBlank(token)){
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        logger.info(body.getCode());
        logger.info(gameCodeService+"-");
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);

        if (body == null || StringUtils.isBlank(body.getCode())) {
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }

        ApiResponseDataMessage apiResponseMessage = gameCodeService.getAward(sessionDomain.getUserId(),body.getCode(),appId);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * 使用兑换码
     * @return
     */
    @RequestMapping(value = "/costCode", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage>costCode(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                        @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                        @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                        @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                        @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                        @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                        @RequestBody GameCodeReqParam body) {
        if(StringUtils.isBlank(token)){
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);

        if (body == null || StringUtils.isBlank(body.getCode())) {
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }
        ApiResponseDataMessage apiResponseMessage = gameCodeService.useCode(sessionDomain.getUserId(),body.getCode(),appId);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }
}
