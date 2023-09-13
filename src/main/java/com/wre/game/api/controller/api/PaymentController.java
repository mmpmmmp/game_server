package com.wre.game.api.controller.api;

import com.wre.game.api.component.DataComponent;
import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.constants.*;
import com.wre.game.api.dao.RechargeDataInfoMapper;
import com.wre.game.api.data.SessionDomain;
import com.wre.game.api.data.entity.RechargeDataInfo;
import com.wre.game.api.data.param.*;
import com.wre.game.api.entity.HWRecahrgeParam;
import com.wre.game.api.entity.KSRechargeParam;
import com.wre.game.api.entity.RechargeCreateInfo;
import com.wre.game.api.exception.ApiException;
import com.wre.game.api.message.ProductSDKMessage;
import com.wre.game.api.message.RechargeMessage;
import com.wre.game.api.service.PaymentService;
import com.wre.game.api.util.Fn;
import com.wre.game.api.util.IdUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * PaymentController
 */
@RestController
@RequestMapping(value = ApiResource.API_ACCOUNTS_PAYMENT)
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Resource
    private SessionComponent sessionComponent;
    @Resource
    private DataComponent dataComponent;

    @Value("${payment.iosType}")
    private Integer iosPaymentType;

    @Resource
    private PaymentService paymentService;

    @Resource
    private RechargeDataInfoMapper rechargeDataInfoMapper;


    /**
     * 创建支付订单
     *
     * @return
     */
    @RequestMapping(value = "/createRecharge", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> createRecharge(
            @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
            @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
            @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
            @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody(required = false) RechargeDataReqParam body) {

        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        logger.info("token:" + token);
        //1. check
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);


        RechargeDataInfo recharge = new RechargeDataInfo();
        recharge.setUserid(body.getUserId());
        recharge.setProductid(body.getProductId());
        recharge.setAmount(body.getAmount());
        recharge.setStatue(RechargeMessage.CRATE_TYPE);
        recharge.setType(Fn.toInt(body.getType()));
        recharge.setShoptype(0);
        recharge.setUugameid(appId);
        recharge.setChannelid(channel);
        recharge.setRoleid(body.getAfId());
        long id = IdUtils.getId();
        recharge.setId(id);
        recharge.setUpdateat(new Date());
        recharge.setCreateat(new Date());
        rechargeDataInfoMapper.insertSelective(recharge);
        RechargeCreateInfo createInfo = new RechargeCreateInfo();
        createInfo.setSerialId(Fn.toString(id));
        RtCode rtCode = RtCode.RT_SUCCESS;
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, createInfo);
        return new ResponseEntity<>(apiResponseDataMessage, rtCode.getHttpStatus());
    }


    /**
     * 安卓内购校验
     *
     * @return
     */
    @RequestMapping(value = "/android", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> androidPay(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                         @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                         @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                         @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                         @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                         @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                         @RequestBody AndroidReqParam body) {
        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);

        if (body == null || StringUtils.isBlank(body.getDataJson())
                || StringUtils.isBlank(body.getSign())) {
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }
        ApiResponseDataMessage apiResponseMessage = paymentService.payAndroid(body, appId);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * 苹果内购校验
     *
     * @return
     */
    @RequestMapping(value = "/ios", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> iosPay(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                     @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                     @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                     @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                     @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                     @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                     @RequestBody PaymentReqParam data) {
        logger.info("request data: {} ", data);
        //1. check
        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);

        if (data == null || StringUtils.isBlank(data.getPayload())
                || StringUtils.isBlank(data.getSerialId())
                || StringUtils.isBlank(data.getTransactionId()) || "(null)".equals(data.getTransactionId())) {
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }
        ApiResponseDataMessage apiResponseMessage = paymentService.payIOS(data);
        logger.info(apiResponseMessage.getData().toString() + "本次数据");
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }


    /**
     * 完成订单
     *
     * @return
     */
    @RequestMapping(value = "/cost", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> cost(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                   @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                   @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                   @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                   @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                   @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                   @RequestBody RechargeCostParam body) {
        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);

        if (body == null || StringUtils.isBlank(body.getSerialId())) {
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }
        ApiResponseDataMessage apiResponseMessage = paymentService.cost(body);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * 补单
     *
     * @return
     */
    @RequestMapping(value = "/add", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> add(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                  @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                  @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                  @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                  @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                  @RequestHeader(value = ApiHeader.TOKEN, required = true) String token) {
        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        if (sessionDomain == null) {

        }
        logger.info("查看userId" + sessionDomain.getUserId());
//        if (body == null || StringUtils.isBlank(body.getSerialId())) {
//            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
//        }
        ApiResponseDataMessage apiResponseMessage = paymentService.add(sessionDomain.getUserId());
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * 完成订单-sdk
     *
     * @return
     */
    @RequestMapping(value = "/costSdk", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> costSdk(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                      @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                      @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                      @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                      @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                      @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                      @RequestBody RechargeCostParam body) {
        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);

        if (body == null || StringUtils.isBlank(body.getSerialId())) {
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }
        ApiResponseDataMessage apiResponseMessage = paymentService.cost(body);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * 苹果内购校验
     *
     * @return
     */
    @RequestMapping(value = "/verify", method = {RequestMethod.POST})
    public SdkResponseDataMessage verify(@RequestParam("AppID") String AppID,
                                         @RequestParam("OrderSerial") String OrderSerial,
                                         @RequestParam("CooperatorOrderSerial") String CooperatorOrderSerial,
                                         @RequestParam("Sign") String Sign,
                                         @RequestParam("Content") String Content) {
        logger.info(AppID + "_" + OrderSerial + "_" + CooperatorOrderSerial + "_" + Sign + "_" + Content);
        if (Fn.isBlank(AppID) ||
                Fn.isBlank(OrderSerial) ||
                Fn.isBlank(CooperatorOrderSerial) ||
                Fn.isBlank(Sign) ||
                Fn.isBlank(Content)) {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultFail, "param is null", "", ProductSDKMessage.Mining.appSecret);
        }

        SdkResponseDataMessage apiResponseMessage = paymentService.sdkVerify(AppID, OrderSerial, CooperatorOrderSerial, Sign, Content);
        logger.info(apiResponseMessage.getResultMsg());
        return apiResponseMessage;
    }


    /**
     * 快手内购校验
     *
     * @return
     */
    @RequestMapping(value = "/createRechargeByKs", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> createRechargeByKs(@RequestHeader(value = ApiHeader.APP_ID, required = true) String appId,
                                                                 @RequestHeader(value = ApiHeader.APP_NAME, required = true) String appName,
                                                                 @RequestHeader(value = ApiHeader.VERSION, required = true) String version,
                                                                 @RequestHeader(value = ApiHeader.CLIENT_IP, required = false) String clientIpAddr,
                                                                 @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
                                                                 @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
                                                                 @RequestBody KSRechargeParam body) {
        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        logger.info("body:" + body);
        if (body == null || StringUtils.isBlank(body.getApp_id())
                || StringUtils.isBlank(body.getChannel_id())
                || StringUtils.isBlank(body.getCurrency_type())
                || StringUtils.isBlank(body.getGame_id())
                || StringUtils.isBlank(body.getMoney())
                || StringUtils.isBlank(body.getProduct_desc())
                || StringUtils.isBlank(body.getProduct_id())
                || StringUtils.isBlank(body.getRole_id())
                || StringUtils.isBlank(body.getRole_level())
                || StringUtils.isBlank(body.getRole_name())
                || StringUtils.isBlank(body.getServer_id())
                || StringUtils.isBlank(body.getServer_name())
                || StringUtils.isBlank(body.getUser_ip())) {
            throw new ApiException(RtCode.RT_PARAMETER_ERROR);
        }
        ApiResponseDataMessage apiResponseMessage = paymentService.createRechargeByKuaishou(body, appId, channel);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }


    /**
     * 快手回调
     *
     * @return
     */
    @RequestMapping(value = "/verifyForKS", method = {RequestMethod.POST})
    public String verifyForKS(@RequestParam("app_id") String app_id,
                              @RequestParam("role_id") String role_id,
                              @RequestParam("server_id") String server_id,
                              @RequestParam("money") String money,
                              @RequestParam("product_id") String product_id,
                              @RequestParam("extension") String extension,
                              @RequestParam("allin_trade_no") String allin_trade_no,
                              @RequestParam("data") String data,
                              @RequestParam("sign") String sign) {
        logger.warn("快手充值回调:" + app_id + "-" + role_id + "-" + server_id + "-" + money + "-" + product_id + "-" + extension + "-" + allin_trade_no + "-" + data + "-" + sign);
        if (Fn.isBlank(app_id) ||
                Fn.isBlank(role_id) ||
                Fn.isBlank(server_id) ||
                Fn.isBlank(money) ||
                Fn.isBlank(product_id) ||
                Fn.isBlank(extension) ||
                Fn.isBlank(allin_trade_no) ||
                Fn.isBlank(data) ||
                Fn.isBlank(sign)) {
            return "param is null";
        }

        String req = paymentService.verifyForKS(app_id, role_id, server_id, money, product_id, extension, allin_trade_no, data, sign);
        return req;
    }

    /**
     * 海外校验
     * @param body
     * @param appId
     * @return
     */
    @RequestMapping(value = "/vertifyForHW", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> vertifyForHW(@RequestBody HWRecahrgeParam body, @RequestHeader(value = ApiHeader.APP_ID, required = true) String appId) {
        if (StringUtils.isEmpty(body.getBody()) || StringUtils.isEmpty(body.getAmount())
                || StringUtils.isEmpty(body.getChannel()) || StringUtils.isEmpty(body.getPurchaseToken()) || StringUtils.isEmpty(body.getSign())
                || StringUtils.isEmpty(body.getUserId()) || StringUtils.isBlank(appId)) {
            RtCode rtCode = RtCode.RT_PARAMETER_ERROR;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        body.setAppId(appId);
        logger.info("receive HW noyify parm:" + body.toString());

        ApiResponseDataMessage apiResponseMessage = paymentService.vertifyForHW(body);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * 完成海外订单
     */
    @RequestMapping(value = "/costByHw", method = {RequestMethod.POST})
    public ResponseEntity<ApiResponseMessage> costByHw(
            @RequestHeader(value = ApiHeader.CHANNEL, required = true) String channel,
            @RequestHeader(value = ApiHeader.TOKEN, required = true) String token,
            @RequestBody HwRechargeCostParam body) {
        logger.info("receive " + body.toString());
        if (StringUtils.isBlank(token)) {
            RtCode rtCode = RtCode.RT_TOKEN_INVALID;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        SessionDomain sessionDomain = sessionComponent.getSessionByChannel(token, channel, false);
        if (ObjectUtils.isEmpty(sessionDomain)) {
            logger.info("获取session失败！" + token);
        }
        if (Fn.isBlank(body.getProductId()) || Fn.isBlank(body.getPurchaseToken())) {
            RtCode rtCode = RtCode.RT_PARAMETER_ERROR;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            return new ResponseEntity<>(apiResponseMessage, rtCode.getHttpStatus());
        }
        ApiResponseDataMessage apiResponseMessage = paymentService.costByHw(body);
        return new ResponseEntity<ApiResponseMessage>(apiResponseMessage, HttpStatus.OK);
    }

    /**
     * 快手订阅回调
     *
     * @return
     */
    @RequestMapping(value = "/verifySubForKS", method = {RequestMethod.POST})
    public String verifySubForKS(@RequestParam("app_id") String app_id,
                                 @RequestParam("role_id") String role_id,
                                 @RequestParam("server_id") String server_id,
                                 @RequestParam("money") String money,
                                 @RequestParam("product_id") String product_id,
                                 @RequestParam("allin_trade_no") String allin_trade_no,
                                 @RequestParam("data") String data,
                                 @RequestParam("extension") String extension,
                                 @RequestParam("notify_detail") String notify_detail,
                                 @RequestParam("sign") String sign) {
        logger.warn("快手订阅充值回调:" + app_id + "-" + role_id + "-" + server_id + "-" + money + "-" + product_id + "-" + allin_trade_no + "-" + data + "-"+extension+"-" +notify_detail+"-" +sign);
        if (Fn.isBlank(app_id) ||
                Fn.isBlank(role_id) ||
                Fn.isBlank(server_id) ||
                Fn.isBlank(money) ||
                Fn.isBlank(product_id) ||
                Fn.isBlank(allin_trade_no) ||
                Fn.isBlank(data) ||
                Fn.isBlank(notify_detail) ||
                Fn.isBlank(sign)) {
            return "param is null";
        }
        return paymentService.verifySubForKS(app_id,role_id,server_id,money,product_id,notify_detail,extension,allin_trade_no,data,sign);
    }

    /**
     * 用户订阅到期时间
     */
    @RequestMapping(value = "/getSBDueTime", method = {RequestMethod.GET})
    public ResponseEntity<ApiResponseMessage> getSBDueTime(
            @RequestParam String roleId,
            @RequestParam String productId) {
        logger.info("Request getSBDueTime Param: roleId: {} , productId:{}", roleId, productId);
        Long subscribeDueTime=paymentService.getSBDueTime(roleId,productId);
        RtCode rtCode = RtCode.RT_SUCCESS;
        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
        ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, subscribeDueTime);
        return new ResponseEntity<>(apiResponseDataMessage, rtCode.getHttpStatus());
    }
}
