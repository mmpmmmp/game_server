package com.wre.game.api.controller.api;

import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.constants.*;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.dto.UserShareFromDTO;
import com.wre.game.api.data.entity.UserRetention;
import com.wre.game.api.data.param.InvitedReqParam;
import com.wre.game.api.exception.ApiException;
import com.wre.game.api.service.UserLoginHistotryService;
import com.wre.game.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * StatController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_STATS)
public class StatController {

    private static final Logger logger = LoggerFactory.getLogger(StatController.class);
    @Autowired
    private SessionComponent sessionComponent;

    @Autowired
    private UserService userService;

    @Autowired
    private UserLoginHistotryService userLoginHistotryService;

    /**
     * 当天分享名单
     * @param channel
     * @return
     */
    @RequestMapping(value = "/shares/today", method = {RequestMethod.GET})
    public List<UserShareFromDTO> getFriends(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam(required = false) String uuid,
            @RequestParam String shareType) {

        logger.info("RequestParam: shareType: {} , uuid:{}", shareType, uuid);

        String fromUuid = uuid;

        if (StringUtils.isBlank(uuid)) {
            try {
                if (StringUtils.isBlank(token)) {
                    return new ArrayList<UserShareFromDTO>();
                }
                SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
                fromUuid = sessionDomain.getUuid();
            } catch (Exception e) {
//                logger.error(e.getMessage());
                return new ArrayList<UserShareFromDTO>();
            }
        }
        return userService.getShareStatisticsFunctionToday(fromUuid, shareType, appId);
    }

    /**
     * 用户已经邀请人数
     * @param appId
     * @param appName
     * @param version
     * @param clientIpAddr
     * @param channel
     * @param token
     * @param uuid
     * @param shareType
     * @return
     */
    @RequestMapping(value = "/getInvitedFriends", method = {RequestMethod.GET})
    public ResponseEntity<ApiResponseMessage> getInvitedFriends(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam String uuid,
            @RequestParam String shareType) {

        logger.info("Request getInvitedFriends Param: shareType: {} , uuid:{}", shareType, uuid);
        String fromUuid = uuid;
        if (StringUtils.isBlank(uuid)) {
            try {
                if (StringUtils.isBlank(token)) {
                    RtCode rtCode = RtCode.RT_TOKEN_INVALID;
                    ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                    return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
                }
                SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
                fromUuid = sessionDomain.getUuid();
            } catch (Exception e) {
                logger.error(e.getMessage());
                RtCode rtCode = RtCode.RT_LOGIN_O_ERROR;
                ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
            }
        }
        int nums = userService.getInvitedFriends(fromUuid, shareType, appId);
        RtCode rtCode = RtCode.RT_SUCCESS;
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, nums);
        return new ResponseEntity<>(apiResponseDataMessage, rtCode.getHttpStatus());
    }

    /**
     * 邀请用户
     * @param appId
     * @param appName
     * @param version
     * @param clientIpAddr
     * @param channel
     * @param token
     * @param invitedReqParam
     * @return
     */
    @RequestMapping(value = "/invitedFriends", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> invitedFriends(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody InvitedReqParam invitedReqParam
            ) {
        logger.info("Request to invitedFriends Param ", invitedReqParam.getUuid(),invitedReqParam.getBody().toString());
        if (StringUtils.isBlank(invitedReqParam.getUuid())) {
            try {
                if (StringUtils.isBlank(token)) {
                    RtCode rtCode = RtCode.RT_TOKEN_INVALID;
                    ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                    return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
                }
                SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
                invitedReqParam.setUuid(sessionDomain.getUuid());
            } catch (Exception e) {
                logger.error(e.getMessage());
                RtCode rtCode = RtCode.RT_LOGIN_O_ERROR;
                ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
            }
        }
        if (StringUtils.isEmpty(invitedReqParam.getUuid())) {
            RtCode rtCode = RtCode.RT_PARAMETER_ERROR;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        return userService.invitedFriends(invitedReqParam,appId);

    }

    /**
     * retentions
     * @return
     */
    @RequestMapping(value = "/retentions", method = {RequestMethod.GET})
    public List<UserRetention> getFriends(
            @RequestParam(required = true) String date,
            @RequestParam String type,
            @RequestParam(required = false) String appName,
            @RequestParam(required = false) String shareType,
            @RequestParam(required = false) String shareFromImage) {

        logger.info("RequestParam: date: {} , type:{}", date, type);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date qDate;
        try {
            qDate = sdf.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }

        if (shareType == null) {
            shareType = "all";
        } else if (StringUtils.equals(shareType, "null")) {
            shareType = null;
        }
        if (shareFromImage == null) {
            shareFromImage = "all";
        } else if (StringUtils.equals(shareFromImage, "null")) {
            shareFromImage = null;
        }
        if (StringUtils.equals(type, ApiConstants.RETENTION_ACTIVE)) {
            return userLoginHistotryService.getActiveUserRentention(qDate, appName, shareType, shareFromImage);
        } else {
            return userLoginHistotryService.getNewUserRentention(qDate, appName, shareType, shareFromImage);
        }

    }


}
