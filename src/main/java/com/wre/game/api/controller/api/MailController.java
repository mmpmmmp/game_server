package com.wre.game.api.controller.api;


import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.constants.*;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.param.MailChangeParam;
import com.wre.game.api.service.MailService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * MailController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_MIAL)
public class MailController {
    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    @Resource
    MailService mailService;

    @Resource
    private SessionComponent sessionComponent;

    /**
     * 获取用户邮件
     */
    @RequestMapping(value = "/getUserMail", method = {RequestMethod.GET})
    public ResponseEntity<ApiResponseMessage> getUserMail(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                          @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                          @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                          @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                          @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                          @RequestParam String countryCode) {
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        return mailService.getUserMail(sessionDomain.getUserId(),appId,countryCode);
    }

    /**
     * 修改用户邮件状态
     */
    @RequestMapping(value = "/changeMailState", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> changeMailState(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                       @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                       @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                       @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                       @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                       @RequestBody MailChangeParam mailChangeParam) {
        logger.info("receive changeMailState mailChangeParam:"+mailChangeParam.toString());
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        if (!mailChangeParam.getUserId().equals(sessionDomain.getUserId())){
             return new ResponseEntity<>( ApiResponseMessageBuilder.build(RtCode.USERID_NOT_MATCH), RtCode.USERID_NOT_MATCH.getHttpStatus());
        }
        ApiResponseDataMessage apiResponseMessage = mailService.changeMailState(mailChangeParam);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }
}

