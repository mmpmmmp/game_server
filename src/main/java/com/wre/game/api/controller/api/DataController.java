package com.wre.game.api.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.adapter.WechatAdapter;
import com.wre.game.api.component.DataComponent;
import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.constants.*;
import com.wre.game.api.dao.UserGameDataDao;
import com.wre.game.api.data.Product;
import com.wre.game.api.data.ProductList;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.dto.UserRankDTO;
import com.wre.game.api.data.dto.UserRanksDTO;
import com.wre.game.api.data.entity.UserGameData;
import com.wre.game.api.data.entity.UserGameDataStorage;
import com.wre.game.api.data.entity.UserGem;
import com.wre.game.api.data.param.DataGameReqParam;
import com.wre.game.api.data.param.DataReqParam;
import com.wre.game.api.data.param.SendGroupReqParam;
import com.wre.game.api.data.param.WechatMessageReqParam;
import com.wre.game.api.service.UserDataService;
import com.wre.game.api.service.UserDataStorageService;
import com.wre.game.api.service.UserService;
import com.wre.game.api.util.CheckoutUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * DataController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_DATA)
public class DataController {

    private static final Logger logger = LoggerFactory.getLogger(DataController.class);

    @Resource
    private SessionComponent sessionComponent;
    @Resource
    private DataComponent dataComponent;
    @Resource
    private UserService userService;
    @Resource
    private UserDataService userDataService;
    @Resource
    private UserGameDataDao userGameDataDao;
    @Resource
    private UserDataStorageService userDataStorageService;

    @Resource
    private WechatAdapter wechatAdapter;

    @Resource
    private GameConfig gameConfig;


    /**
     * 上传用户数据
     * @return
     */
    @RequestMapping(value = "/game", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> uploadGameData(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody(required = false) DataGameReqParam data) {

        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        //1. check
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);


        if (data == null || StringUtils.isBlank(data.getUuid()) || StringUtils.isBlank(data.getAppId())) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        //2. set game data ( mirgate data from redis to mysql)
        // 2.1 save data to Mysql
        UserGameDataStorage userGameDataStorage = new UserGameDataStorage();
        userGameDataStorage.setAppId(data.getAppId());
        userGameDataStorage.setAppName(appName);
        userGameDataStorage.setUuid(data.getUuid());
        userGameDataStorage.setDataStorage(data.getData());
        userDataStorageService.saveUserGameDataStorage(userGameDataStorage);
        // 2.2 del redis data
        dataComponent.delGameDataKey(data.getUuid(), data.getAppId());

        RtCode rtCode = RtCode.RT_SUCCESS;
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
    }

    /**
     * 获取用户数据 V1
     */
    @RequestMapping(value = "/game", method = {RequestMethod.GET})
    public String getGameData(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appIdHeader,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam String appId,
            @RequestParam String uuid) {
        if (StringUtils.isBlank(token)) {
            return "";
        }
        //1. get uuid
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        //2. get game data
        // 2.1 get dataStorage from Mysql
        UserGameDataStorage userGameDataStorage = userDataStorageService.getUserGameDataStorage(uuid, appId);
        String dataStorage = userGameDataStorage != null ? userGameDataStorage.getDataStorage() : "";

        // 2.2 if dataStorage is empty, get from Redis
        if (StringUtils.isEmpty(dataStorage)) {
            dataStorage = dataComponent.getGameData(uuid, appId);
        }

        return dataStorage;
    }

    /**
     * 获取用户数据 V2
     */
    @RequestMapping(value = "/getGameData", method = {RequestMethod.GET})
    public ResponseEntity<ApiResponseMessage> getGameDataV2(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appIdHeader,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam String appId,
            @RequestParam String uuid) {
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        //1. get uuid
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        //2. get game data
        // 2.1 get dataStorage from Mysql
        UserGameDataStorage userGameDataStorage = userDataStorageService.getUserGameDataStorage(uuid, appId);
        String dataStorage = !ObjectUtils.isEmpty(userGameDataStorage) ? userGameDataStorage.getDataStorage() : "";
        // 2.2 if dataStorage is empty, get from Redis
        if (StringUtils.isEmpty(dataStorage)) {
            dataStorage = dataComponent.getGameData(uuid, appId);
        }
        if (StringUtils.isEmpty(dataStorage)) {
            dataStorage = "";
        }
        return new ResponseEntity<ApiResponseMessage>(new ApiResponseDataMessage(
                new ApiResponseMessage("200", "success", "200", "success"),
                dataStorage), HttpStatus.OK);
    }

    /**
     * 获取用户数据 V3
     * @param appIdHeader  登录游戏ID
     * @param appName      游戏名称
     * @param version      游戏版本
     * @param clientIpAddr 设备Ip
     * @param channel      渠道
     * @param token        身份验证票据
     * @param appId        appId（更新版本上传的）
     * @param uuid         uuid（更新版本上传）
     * @param userId       用户id
     * @return 返回用户数据
     */

    @RequestMapping(value = "/getGameDataByUserId", method = {RequestMethod.GET})
    public ResponseEntity<ApiResponseMessage> getGameDataV3(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appIdHeader,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam String appId,
            @RequestParam String uuid,
            @RequestParam String userId) {
        //身份验证
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        //1. get uuid
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        //2. get game data
        // 2.1 get dataStorage from Mysql
        UserGameDataStorage userGameDataStorage = userDataStorageService.getUserGameDataStorage(uuid, appId);
        if (userGameDataStorage == null || StringUtils.isEmpty(userGameDataStorage.getDataStorage())) {
            // 如果使用uuid查询不到用户游戏数据，则会被调用
            // 使用userId再次查询用户信息
            userGameDataStorage = userDataStorageService.getUserGameDataStorageByUserId(userId, appId);
        }
        String dataStorage = !ObjectUtils.isEmpty(userGameDataStorage) ? userGameDataStorage.getDataStorage() : "";
        // 2.2 if dataStorage is empty, get from Redis
        if (StringUtils.isEmpty(dataStorage)) {
            dataStorage = dataComponent.getGameData(uuid, appId);
        }
        if (StringUtils.isEmpty(dataStorage)) {
            dataStorage = "";
        }
        return new ResponseEntity<ApiResponseMessage>(new ApiResponseDataMessage(
                new ApiResponseMessage("200", "success", "200", "success"),
                dataStorage), HttpStatus.OK);
    }


    /**
     * 报错用户数据
     * @return
     */
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> uploadGameData(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody(required = false) DataReqParam data) {
        if (!StringUtils.isBlank(token) && !StringUtils.isBlank(channel)) {
            Long userId = sessionComponent.getUserIdByChannel(token, channel);
        } else {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        if (data == null || StringUtils.isBlank(data.getUuid()) || StringUtils.isBlank(data.getAppId())) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        //2. set game data

        UserGameData ugd = new UserGameData();
        ugd.setScore(data.getScore());
        ugd.setAppName(appName);
        ugd.setParam1(data.getParam1());
        ugd.setParam2(data.getParam2());
        ugd.setParam3(data.getParam3());
        ugd.setParam4(data.getParam4());
        ugd.setParam5(data.getParam5());
        ugd.setUuid(data.getUuid());
        ugd.setAppId(data.getAppId());

        RtCode rtCode = userDataService.saveUserGameData(ugd);
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
    }

    /**
     * 获取用户排行
     */
    @RequestMapping(value = "/rank", method = {RequestMethod.GET})
    public UserRanksDTO getRank(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam Integer limit) {

        //1. check
//        Long userId = sessionComponent.getUserIdByChannel(token, channel);


        List<UserRankDTO> list = userGameDataDao.selectGameDataRank(appId, limit);

        UserRanksDTO userRanksDTO = new UserRanksDTO();
        userRanksDTO.setRankList(list);

        return userRanksDTO;
    }

    /**
     * 发送微信消息
     */
    @RequestMapping(value = "/sendWechatGroup", method = {RequestMethod.POST})
    public JSONObject sendGroupMessage(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody SendGroupReqParam reqParam) {

        JSONObject obj = sendWechatMessage(appId, reqParam.getOpenId());
        return obj;
    }

    /**
     * 接收微信消息
     */
    @RequestMapping(value = "/receiveWechatMessage", method = {RequestMethod.POST})
    public String receiveWechatMessage(
            @RequestBody WechatMessageReqParam reqParam) {
        logger.info("receiveWechatMessage POST -> {}", reqParam.toString());
        JSONObject obj = new JSONObject();
        //小程序类型
        if (StringUtils.equalsIgnoreCase(reqParam.getMsgType(), ApiConstants.WechatMessageType.MINI_PROGRAM_PAGE)) {
            obj = sendWechatMessage(reqParam.getAppId(), reqParam.getFromUserName());
        }
        logger.info("sendWenchatMessage -> {}", obj.toString());
        return "success";
    }

    /**
     * 校验并获取微信参数
     */
//    @RequestMapping(value = "/receiveWechatMessage", method = {RequestMethod.GET})
//    public String receiveWechatMessage(
//            @RequestParam String signature,
//            @RequestParam String timestamp,
//            @RequestParam String nonce,
//            @RequestParam String echostr
//    ) {
//        logger.info("receiveWechatMessage GET -> {}, {}, {}, {}", signature, timestamp, nonce, echostr);
//        // 随机字符串
//        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
//        if (signature != null && CheckoutUtil.checkSignature(signature, timestamp, nonce)) {
//            return echostr;
//
//        }
//        return "";
//    }

    /**
     * 游戏配置列表
     */
    @RequestMapping(value = "/products", method = {RequestMethod.GET})
    public String getProductListByAppId(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appIdHeader,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam String appId) {

        logger.info("request data: appId {} ", appId);

        ProductList productList = gameConfig.productList();
        Map<String, Product> m = productList.getProductListMap().get(appId);
        System.out.println(m);
        if (m == null || m.isEmpty()) {
            return "{}";
        } else {

            return JSONObject.toJSONString(m);
        }
    }

    private JSONObject sendWechatMessage(String appId, String openId) {
        JSONObject obj = new JSONObject();
        //1. get qr code url
        String qrcodeUrl = wechatAdapter.getGroupQrCodeLink(openId);
        //2. get access token
        String accessToken = dataComponent.getAppAccessToken(appId);
        System.out.println(accessToken);
        if (StringUtils.isBlank(accessToken)) {
            logger.info("get new accessToken");
            accessToken = wechatAdapter.getAccessToken(appId);
            dataComponent.setAppAccessToken(appId, accessToken, 3600);// access token expire 1 hour
        }

        //3. send group message
        if (StringUtils.isBlank(qrcodeUrl)) {
            // send text
            wechatAdapter.sendMessageByText(openId, ApiConstants.WECHAT_CONTENT_GROUP_NOT_AVAILABLE, accessToken);
        } else {
            // send text
            wechatAdapter.sendMessageByText(openId, ApiConstants.WECHAT_CONTENT, accessToken);
            // send qr code
            String mediaId = wechatAdapter.uploadQrCodeAndGetMediaId(openId, qrcodeUrl, accessToken);
            obj = wechatAdapter.sendMessageByMediaId(openId, mediaId, accessToken);
        }

        obj.put("qrcode_url", qrcodeUrl);
        return obj;
    }

    /**
     * 清理用户游戏数据
     */
    @RequestMapping(value = "/clear", method = {RequestMethod.GET})
    public ResponseEntity<ApiResponseMessage> clearGameData(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appIdHeader,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestParam String uuid) {
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        //1. get uuid
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);

        if (!sessionDomain.getUuid().equals(uuid) && !sessionDomain.getUserId().equals(uuid)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.UUID_NOT_MATCH), RtCode.UUID_NOT_MATCH.getHttpStatus());
        }
        logger.warn(uuid + "清除游戏数据......");
        return userDataService.clearGameData(uuid);
    }

    /**
     * 获取用户的钻石数
     */
    @GetMapping("/gem")
    public ResponseEntity<ApiResponseMessage> getGem(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token) {
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        Long userId = sessionComponent.getUserIdByChannel(token, channel);
        if (userId == null) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.USERID_NOT_MATCH), RtCode.USERID_NOT_MATCH.getHttpStatus());
        }
        UserGem userGem = userService.getUserGem(userId, appId);
        return new ResponseEntity<>(new ApiResponseDataMessage(RtCode.RT_SUCCESS,
                Optional.ofNullable(userGem.getGem()).orElse(0L)), RtCode.RT_SUCCESS.getHttpStatus());
    }

    /**
     * 用户钻石增减
     */
    @GetMapping("/gem/{action}")
    public ResponseEntity<ApiResponseMessage> gem(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @PathVariable("action") String action,
            @RequestParam("gem") Integer gem) {
        if (StringUtils.isBlank(token)) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.RT_PARAMETER_ERROR), RtCode.RT_PARAMETER_ERROR.getHttpStatus());
        }
        Long userId = sessionComponent.getUserIdByChannel(token, channel);
        if (userId == null) {
            return new ResponseEntity<>(ApiResponseMessageBuilder.build(RtCode.USERID_NOT_MATCH), RtCode.USERID_NOT_MATCH.getHttpStatus());
        }
        boolean result = false;
        switch (action) {
            case "add":
                result = userService.addUserGem(userId, appId, gem);
                break;
            case "plus":
                result = userService.plusUserGem(userId, appId, gem);
                break;
        }
        if (result) {
            return new ResponseEntity<>( ApiResponseMessageBuilder.build(RtCode.RT_SUCCESS), RtCode.RT_SUCCESS.getHttpStatus());
        }
        return new ResponseEntity<>( ApiResponseMessageBuilder.build(RtCode.RT_INTERNAL_ERROR), RtCode.RT_INTERNAL_ERROR.getHttpStatus());
    }
}
