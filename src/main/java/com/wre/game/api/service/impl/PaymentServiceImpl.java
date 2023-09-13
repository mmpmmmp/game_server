package com.wre.game.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.constants.*;
import com.wre.game.api.dao.RechargeDataInfoMapper;
import com.wre.game.api.dao.UserDao;
import com.wre.game.api.dao.UserSubscribeDao;
import com.wre.game.api.data.GameInfo;
import com.wre.game.api.data.entity.*;
import com.wre.game.api.data.param.*;
import com.wre.game.api.entity.*;
import com.wre.game.api.message.ProductSDKMessage;
import com.wre.game.api.message.RechargeMessage;
import com.wre.game.api.service.PaymentService;
import com.wre.game.api.util.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

@Component
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);


    @Resource
    private RechargeDataInfoMapper dataInfoMapper;
    @Resource
    private UserDao userDao;
    @Resource
    UserSubscribeDao userSubscribeDao;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public ApiResponseDataMessage payIOS(PaymentReqParam data) {
        try {
            logger.info("充值参数:" + data);
            String environment="PRODUCTION";
            RechargeDataInfo bean = dataInfoMapper.selectByPrimaryKey(Long.valueOf(data.getSerialId()));
            if (bean == null) {
                logger.error("充值无此订单:" + JSON.toJSONString(data.getSerialId()));
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no order", "202", "no order"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            //线上环境验证
            String verifyResult = IosVerifyUtil.buyAppVerify(data.getPayload(), 1);
            if (verifyResult == null) {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "苹果验证失败，返回数据为空", "201", "苹果验证失败，返回数据为空"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            } else {
                JSONObject appleReturn = JSONObject.parseObject(verifyResult);
                String states = appleReturn.getString("status");
                logger.info("苹果平台返回值：appleReturn" + appleReturn);
                //无数据则沙箱环境验证
                if ("21007".equals(states)) {
                    environment="SAND_BOX";
                    verifyResult = IosVerifyUtil.buyAppVerify(data.getPayload(), 0);
                    logger.info("沙盒环境，苹果平台返回JSON:" + verifyResult);
                    appleReturn = JSONObject.parseObject(verifyResult);
                    states = appleReturn.getString("status");
                }
                /**验证订单状态*/
                if (!"0".equals(states)) {
                    return new ApiResponseDataMessage(new ApiResponseMessage("200", "支付失败，错误码：" + states, "204", "支付失败，错误码：" + states), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
                }
                /**验证订单在苹果是否存在*/
                IosRechargeParam iosParam = JSONObject.parseObject(appleReturn.getString("receipt"), IosRechargeParam.class);
                iosParam.ListToMap();
                IosRechargeInApp iosInApp = iosParam.getIn_app_map().get(data.getTransactionId());
                if (iosInApp == null) {
                    return new ApiResponseDataMessage(new ApiResponseMessage("200", "no transactionId", "206", "no transactionId"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
                }
                /**验证本地订单状态*/
                if (bean.getStatue().equals(RechargeMessage.CRATE_TYPE)) {
                    bean.setOrderid(data.getTransactionId());
                    bean.setStatue(RechargeMessage.PAY_TYPE);
                    bean.setUpdateat(new Date());
                    dataInfoMapper.updateByPrimaryKeySelective(bean);
                    RtCode rtCode = RtCode.RT_SUCCESS;
                    ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                    ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, new RechargeAndroidRes(RechargeMessage.Android.STATE_NO,environment));
                    return apiResponseDataMessage;
                } else if (bean.getStatue().equals(RechargeMessage.PAY_TYPE)) {
                    return new ApiResponseDataMessage(new ApiResponseMessage("200", "verified", "203", "verified"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF,environment));
                } else {
                    return new ApiResponseDataMessage(new ApiResponseMessage("200", "cost", "205", "cost"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF,environment));
                }
            }
        } catch (Exception e) {
            logger.error("充值订单解析失败：" + JSON.toJSONString(data) + e.getMessage(), e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail", "fail", "204"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public ApiResponseDataMessage payAndroid(AndroidReqParam body, String appId) {
        try {
            GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(appId);
            if (gameInfo == null) {
                return new ApiResponseDataMessage(new ApiResponseMessage("500",
                        "appId no config", "501", "appId no config"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            String dataSing = body.getDataJson();
            String sign = body.getSign();
            String publicKey = gameInfo.getPublicKey();
            if (StringUtils.isBlank(publicKey)) {
                return new ApiResponseDataMessage(new ApiResponseMessage("500", "publicKey no config", "502", "publicKey no config"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            if (RSASignature.doCheck(dataSing, sign, publicKey)) {
                logger.info("Result : 验签成功！");
            } else {
                logger.info("Result : 验签失败！");
                logger.error("充值RSA验证失败:" + JSON.toJSONString(body) + "---" + appId + "---" + publicKey);
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "verification failed", "201", "verification failed"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            RechargeAndroidInfo rechargeAndroidInfo = JSON.parseObject(body.getDataJson(), RechargeAndroidInfo.class);
            if (!rechargeAndroidInfo.getObfuscatedProfileId().equals(body.getSerialId())) {
                logger.error("充值订单错误:" + JSON.toJSONString(body.getSerialId()) + "充值:" + rechargeAndroidInfo.getObfuscatedProfileId());
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "order error", "208", "order error"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            RechargeDataInfo bean = dataInfoMapper.selectByPrimaryKey(Long.valueOf(rechargeAndroidInfo.getObfuscatedProfileId()));
            if (bean == null) {
                logger.error("充值无此订单:" + JSON.toJSONString(body.getSerialId()));
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no order", "202", "no order"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            if (bean.getStatue() == 0) {
                bean.setOrderid(rechargeAndroidInfo.getOrderId());
                bean.setStatue(RechargeMessage.PAY_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                RtCode rtCode = RtCode.RT_SUCCESS;
                ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
                return apiResponseDataMessage;
            } else if (bean.getStatue() == 1) {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "verified", "203", "verified"), new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
            } else {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "cost", "205", "cost"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
        } catch (Exception e) {
            logger.error("充值订单解析失败：" + JSON.toJSONString(body) + e.getMessage(), e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail", "204", "fail"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public ApiResponseDataMessage cost(RechargeCostParam body) {
        try {
            logger.warn(JSON.toJSONString(body)+"开始消耗");
            RechargeDataInfo bean = dataInfoMapper.selectByPrimaryKey(Long.valueOf(body.getSerialId()));
            if (bean == null) {
                logger.error("充值无此订单:" + JSON.toJSONString(body.getSerialId()));
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no order", "202", "no order"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            if (bean.getStatue().equals(0)) {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no verified", "201", "no verified"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            if (bean.getStatue().equals(1)) {
                bean.setStatue(RechargeMessage.AWARD_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                RtCode rtCode = RtCode.RT_SUCCESS;
                ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
                return apiResponseDataMessage;
            } else {
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "cost", "205", "cost"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
        } catch (Exception e) {
            logger.error(JSON.toJSONString(body)+"消耗失败："  + e.getMessage(), e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail", "204", "fail"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
    }


    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public ApiResponseDataMessage costSdk(RechargeCostParam body) {
        try {
            RechargeDataInfo bean = dataInfoMapper.selectByPrimaryKey(Long.valueOf(body.getSerialId()));
            if (bean == null) {
                logger.error("充值无此订单:" + JSON.toJSONString(body.getSerialId()));
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no order", "202", "no order"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            if (bean.getStatue() == 1) {
                bean.setStatue(RechargeMessage.AWARD_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                RtCode rtCode = RtCode.RT_SUCCESS;
                ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
                return apiResponseDataMessage;
            } else {
                bean.setStatue(RechargeMessage.COST_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "cost", "205", "cost"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
        } catch (Exception e) {
            logger.error("充值订单解析失败：" + JSON.toJSONString(body) + e.getMessage(), e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail", "204", "fail"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
    }

    @Override
    public ApiResponseDataMessage add(String userId) {
        try {
            List<AddRecharge> list = dataInfoMapper.selectByUserIdAndAdd(userId);
            RtCode rtCode = RtCode.RT_SUCCESS;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, list);
            return apiResponseDataMessage;
        } catch (Exception e) {
            logger.error("订单解析失败：" + JSON.toJSONString(userId) + e.getMessage(), e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail", "204", "fail"), null);
    }


    @Override
    public SdkResponseDataMessage sdkVerify(String AppID,
                                            String OrderSerial,
                                            String CooperatorOrderSerial,
                                            String Sign,
                                            String Content) {
        /**验证订单信息*/
        Boolean md5Sign = BaiduSdkRechargeUtil.md5Sign(AppID, OrderSerial, CooperatorOrderSerial, Sign, Content);
        if (!md5Sign) {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultVerify, "sign error", "", ProductSDKMessage.Mining.appSecret);
        }
        /**获取订单数据*/
        VerifySdkBaidu verifySdkBaidu = BaiduSdkRechargeUtil.getData(AppID, OrderSerial, CooperatorOrderSerial, Sign, Content);

        RechargeDataInfo bean = dataInfoMapper.selectByPrimaryKey(Long.valueOf(verifySdkBaidu.getCooperatorOrderSerial()));
        if (bean == null) {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultVerify, "no Product", "", ProductSDKMessage.Mining.appSecret);
        }
        /**验证订单信息是否正确*/
        if (!ProductSDKMessage.Mining.appId.equals(verifySdkBaidu.getAppId())) {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultVerify, "AppId fail", "", ProductSDKMessage.Mining.appSecret);
        }
        /**验证金额*/
        if (!bean.getAmount().equals(Fn.toString(verifySdkBaidu.getContent().getOrderMoney()))) {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultVerify, "amount fail", "", ProductSDKMessage.Mining.appSecret);
        }
        /**验证用户*/
        if (!bean.getUserid().equals(verifySdkBaidu.getContent().getUID())) {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultVerify, "uid fail", "", ProductSDKMessage.Mining.appSecret);
        }

        if (bean.getStatue().equals(RechargeMessage.CRATE_TYPE)) {
            bean.setOrderid(verifySdkBaidu.getOrderSerial());
            bean.setStatue(RechargeMessage.PAY_TYPE);
            bean.setUpdateat(new Date());
            dataInfoMapper.updateByPrimaryKeySelective(bean);
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultSuccess, "success", "", ProductSDKMessage.Mining.appSecret);
        } else if (bean.getStatue().equals(RechargeMessage.PAY_TYPE)) {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultSuccess, "verify", "", ProductSDKMessage.Mining.appSecret);
        } else if (bean.getStatue().equals(RechargeMessage.COST_TYPE)) {
            bean.setOrderid(verifySdkBaidu.getOrderSerial());
            bean.setStatue(RechargeMessage.AWARD_TYPE);
            bean.setUpdateat(new Date());
            dataInfoMapper.updateByPrimaryKeySelective(bean);
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultSuccess, "award", "", ProductSDKMessage.Mining.appSecret);
        } else {
            return new SdkResponseDataMessage(Fn.toInt(AppID), ProductSDKMessage.ResultCode.ResultSuccess, "cost", "", ProductSDKMessage.Mining.appSecret);
        }
    }

    @Override
    public ApiResponseDataMessage createRechargeByKuaishou(KSRechargeParam body, String appId, String channel) {

        try {
            /**验证id和userId是否正确*/
            User user = userDao.selectUserByOpenIdAndAppId(body.getGame_id(), appId);
            if (user == null) {
                logger.error("该角色不存在:" + JSON.toJSONString(body.getGame_id()));
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no order", "251", "no role"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            String userString = Fn.toString(user.getUserId());
            if (!userString.equals(body.getRole_id())) {
                logger.error("和游戏账号角色不匹配:" + body.getRole_id() + "_" + JSON.toJSONString(user.getUserId()));
                return new ApiResponseDataMessage(new ApiResponseMessage("200", "no order", "252", "no gameRole"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            RechargeDataInfo recharge = new RechargeDataInfo();
            recharge.setUserid(body.getRole_id());
            recharge.setProductid(body.getProduct_id());
            recharge.setAmount(String.valueOf(Double.valueOf(body.getMoney()).intValue()));
            recharge.setStatue(RechargeMessage.CRATE_TYPE);
            recharge.setType(1);
            recharge.setShoptype(0);
            recharge.setUugameid(body.getApp_id());
            recharge.setChannelid(channel);
            recharge.setRoleid(body.getGame_id());
            long id = IdUtils.getId();
            recharge.setId(id);
            recharge.setUpdateat(new Date());
            recharge.setCreateat(new Date());
            dataInfoMapper.insertSelective(recharge);
            TreeMap<String,Object> map=new TreeMap<>();
            /**1.添加回调地址:*/
            if (StringUtils.isNotEmpty(body.getSubscribe())&&body.getSubscribe().equals("1"))
            {
                Map<String,Object> summap=new HashMap<>();
                body.setNotify_url(ProductSDKMessage.SUBSCRIBE_NOTYFY_URL);
                map.put("ks_order_type","subscribe");
                summap.put("product_id",body.getProduct_id());
                summap.put("days",Integer.valueOf(body.getDays()));
                summap.put("notify_url",ProductSDKMessage.SUBSCRIBE_NOTYFY_URL);
                summap.put("product_name",body.getProduct_id());
                summap.put("first_price",0);
                summap.put("price",Integer.valueOf(body.getMoney()));
                summap.put("serialId",Fn.toString(id));
                map.put("ks_subscribe_product_info",summap);
                /**2.添加订单号:*/
                body.setExtension(JSON.toJSONString(map));
                UserSubscribe userSubscribe=new UserSubscribe();
                userSubscribe.setUserId(body.getRole_id());
                userSubscribe.setId(Fn.toString(id));
                userSubscribe.setAppId(appId);
                userSubscribe.setProductId(body.getProduct_id());
                userSubscribe.setSubscribeduetime(0L);
                userSubscribeDao.insert(userSubscribe);
            }else {
                body.setNotify_url(ProductSDKMessage.NOTIFY_URL);
                body.setExtension(Fn.toString(id));
            }

            /**添加订单号*/
            body.setThird_party_trade_no(Fn.toString(id));
            /**3.转换sign*/
            String dataJson = body.toSign();
            KSRechargeReq result = new KSRechargeReq();
            result.setDataString(dataJson);
            result.setSign(SHA512withRSA.signMethodToString(dataJson, ProductSDKMessage.AssemblyLine.appPublicKey));
            /**返回成功*/
            RtCode rtCode = RtCode.RT_SUCCESS;
            ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
            ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, result);
            return apiResponseDataMessage;
            /**加密*/
        } catch (Exception e) {
            logger.error("充值订单解析失败：" + JSON.toJSONString(body) + e.getMessage(), e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("200", "fail", "204", "fail"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
    }

    @Override
    public String verifyForKS(String app_id,
                              String role_id,
                              String server_id,
                              String money,
                              String product_id,
                              String extension,
                              String allin_trade_no,
                              String data,
                              String ksSign) {
        KSRechargeCallBackParam body = new KSRechargeCallBackParam();

        try {
            GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(ProductSDKMessage.AssemblyLine.app_id);
            if (gameInfo == null) {
                return "no appId";
            }
            body.setAllin_trade_no(allin_trade_no);
            body.setApp_id(app_id);
            body.setData(data);
            body.setExtension(extension);
            body.setMoney(money);
            body.setProduct_id(product_id);
            body.setRole_id(role_id);
            body.setServer_id(server_id);
            body.setSign(ksSign);
            /**验证订单*/
            String dataString = body.toSign();
            String sign = body.getSign();
            if (!SHA512withRSA.verifyMethodToString(dataString, sign, gameInfo.getPublicKey())) {
                logger.info("快手验签成功");
            } else {
                logger.error("验签失败" + body.toString() + "_" + ProductSDKMessage.AssemblyLine.app_id);
                return "sign fail";
            }

            RechargeDataInfo bean = dataInfoMapper.selectByPrimaryKey(Long.valueOf(body.getExtension()));
            if (bean == null) {
                return "no order";
            }
            /**验证订单信息是否正确*/
            if (!ProductSDKMessage.AssemblyLine.ks_appId.equals(body.getApp_id())) {
                return "AppId fail";
            }

            /**验证金额*/
            if (!bean.getAmount().equals(body.getMoney())) {
                return "Money fail";
            }
            /**验证用户*/
            if (!bean.getUserid().equals(body.getRole_id())) {
                return "RoleId fail";
            }

            if (bean.getStatue().equals(RechargeMessage.CRATE_TYPE)) {
                bean.setOrderid(body.getAllin_trade_no());
                bean.setStatue(RechargeMessage.PAY_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                return "success";
            } else if (bean.getStatue().equals(RechargeMessage.PAY_TYPE)) {
                return "success";
            } else if (bean.getStatue().equals(RechargeMessage.COST_TYPE)) {
                bean.setOrderid(body.getAllin_trade_no());
                bean.setStatue(RechargeMessage.AWARD_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                return "success";
            } else if (bean.getStatue().equals(RechargeMessage.AWARD_TYPE)){
                return "success";
            }
            else {
                return "fail";
            }
        } catch (Exception e) {
            logger.error("充值订单解析失败：" + JSON.toJSONString(body) + e.getMessage(), e);
            return "fail";
        }

    }

    @Override
    public ApiResponseDataMessage vertifyForHW(HWRecahrgeParam body) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = org.apache.commons.codec.binary.Base64.decodeBase64(ProductSDKMessage.AssemblyLine.HW_PUBLIC_KEY);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
            java.security.Signature signature;
            signature = java.security.Signature.getInstance("SHA256WithRSA");
            signature.initVerify(pubKey);
            signature.update(body.getBody().getBytes(StandardCharsets.UTF_8));
            byte[] bsign = Base64.decodeBase64(body.getSign());
            boolean verify = signature.verify(bsign);
            JSONObject jsonObject = JSONObject.parseObject(body.getBody());

            if (verify) {
                logger.info(body.getPurchaseToken()+"vertify success!");
                RechargeDataInfo rechargeDataInfo = dataInfoMapper.selectByRoleId(body.getPurchaseToken());
                if (ObjectUtils.isEmpty(rechargeDataInfo)){
                    RechargeDataInfo bean = new RechargeDataInfo();
                    bean.setCreateat(new Date());
                    bean.setUpdateat(new Date());
                    bean.setOrderid(String.valueOf(jsonObject.get("orderId")));
                    bean.setChannelid(body.getChannel());
                    bean.setProductid(String.valueOf(jsonObject.get("productId")));
                    bean.setUugameid(body.getAppId());
                    bean.setShoptype(0);
                    bean.setType(1);
                    bean.setUserid(body.getUserId());
                    bean.setRoleid(body.getPurchaseToken());
                    bean.setAmount(body.getAmount());
                    long id = IdUtils.getId();
                    bean.setId(id);
                    bean.setStatue(RechargeMessage.PAY_TYPE);
                    dataInfoMapper.insertSelective(bean);
                }

                RtCode rtCode = RtCode.RT_SUCCESS;
                ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
                return apiResponseDataMessage;
            } else {
                logger.error(body.getPurchaseToken()+"vertify success!");
                return new ApiResponseDataMessage(new ApiResponseMessage("500", "fail", "204", "fail"), "fail");
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            return new ApiResponseDataMessage(new ApiResponseMessage("500", "fail", "204", "fail"), "fail");
        }

    }

    @Override
    public ApiResponseDataMessage costByHw(HwRechargeCostParam body) {
        try {
            RechargeDataInfo bean = dataInfoMapper.selectByRoleId(body.getPurchaseToken());
            if (bean == null) {
                logger.error("充值无此订单:" + JSON.toJSONString(body.getPurchaseToken()));
                return new ApiResponseDataMessage(new ApiResponseMessage("500", "no order", "202", "no order"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
            if (bean.getStatue() == 1) {
                //向华为发送确认消耗
//                String appAt = HwAtUtil.getAppAT();
//                Map<String, String> headers = HwAtUtil.buildAuthorization(appAt);
//                Map<String, Object> bodyMap = new HashMap<>();
//                bodyMap.put("purchaseToken", body.getPurchaseToken());
//                bodyMap.put("productId", body.getProductId());
//                String msgBody = JSONObject.toJSONString(bodyMap);
//                String response = HwAtUtil.httpPost(ProductSDKMessage.AssemblyLine.HW_ROOT_URL + "/applications/v2/purchases/confirm",
//                        "application/json; charset=UTF-8", msgBody, 5000, 5000, headers);
//                logger.info("HW confirmPurchase response:" + response);
                bean.setStatue(RechargeMessage.AWARD_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                RtCode rtCode = RtCode.RT_SUCCESS;
                ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);
                ApiResponseDataMessage apiResponseDataMessage = new ApiResponseDataMessage(apiResponseMessage, new RechargeAndroidRes(RechargeMessage.Android.STATE_NO));
                return apiResponseDataMessage;
            } else {//没什么用
                return new ApiResponseDataMessage(new ApiResponseMessage("500", "fail", "205", "statue is not PAY_TYPE "), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
            }
        } catch (Exception e) {
            logger.error("充值订单解析失败：" + JSON.toJSONString(body) + e.getMessage(), e);
        }
        return new ApiResponseDataMessage(new ApiResponseMessage("500", "fail", "204", "fail"), new RechargeAndroidRes(RechargeMessage.Android.STATE_OFF));
    }

    @Override
    public String verifySubForKS(String app_id, String role_id, String server_id, String money, String product_id, String notify_detail, String extension,String allin_trade_no, String data, String ksSign) {
        KSRechargeCallBackParam body = new KSRechargeCallBackParam();
        try {
            GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(ProductSDKMessage.AssemblyLine.app_id);
            if (gameInfo == null) {
                return "no appId";
            }
            body.setAllin_trade_no(allin_trade_no);
            body.setApp_id(app_id);
            body.setData(data);
            body.setNotifyDetail(notify_detail);
            body.setMoney(money);
            body.setProduct_id(product_id);
            body.setRole_id(role_id);
            body.setServer_id(server_id);
            body.setExtension(extension);
            body.setSign(ksSign);
            if (data!=null){
                /**验证订单*/
                String dataString = body.toSign();
                String sign = body.getSign();
                if (!SHA512withRSA.verifyMethodToString(dataString, sign, gameInfo.getPublicKey())) {
                    logger.info("快手订阅验签成功");
                } else {
                    logger.error("验签失败" + body.toString() + "_" + ProductSDKMessage.AssemblyLine.app_id);
                    return "sign fail";
                }
            }
            UserSubscribeKey userSubscribeKey=new UserSubscribeKey();
            userSubscribeKey.setProductId(body.getProduct_id());
            userSubscribeKey.setUserId(body.getRole_id());
            UserSubscribe userSubscribe = userSubscribeDao.selectByPrimaryKey(userSubscribeKey);
            RechargeDataInfo bean = dataInfoMapper.selectByPrimaryKey(Long.valueOf(userSubscribe.getId()));
            if (bean == null) {
                return "no order";
            }
            /**验证订单信息是否正确*/
            if (!ProductSDKMessage.AssemblyLine.ks_appId.equals(body.getApp_id())) {
                return "AppId fail";
            }

            /**验证金额*/
            if (!bean.getAmount().equals(body.getMoney())) {
                return "Money fail";
            }
            /**验证用户*/
            if (!bean.getUserid().equals(body.getRole_id())) {
                return "RoleId fail";
            }
            JSONObject notifyDetail = JSON.parseObject(body.getNotifyDetail());
            logger.info("notify_detail:"+notify_detail);
            Long subscribe_due_time = Long.valueOf(String.valueOf(notifyDetail.get("subscribe_due_time")));
            logger.info("subscribe_due_time:"+subscribe_due_time);
            if (bean.getStatue().equals(RechargeMessage.CRATE_TYPE)) {
                bean.setOrderid(body.getAllin_trade_no());
                bean.setStatue(RechargeMessage.PAY_TYPE);
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                userSubscribe.setSubscribeduetime(subscribe_due_time);
                userSubscribeDao.updateByPrimaryKeySelective(userSubscribe);
                return "success";
            } else if (bean.getStatue().equals(RechargeMessage.PAY_TYPE)) {
                bean.setUpdateat(new Date());
                dataInfoMapper.updateByPrimaryKeySelective(bean);
                userSubscribe.setSubscribeduetime(subscribe_due_time);
                userSubscribeDao.updateByPrimaryKeySelective(userSubscribe);
                return "success";
            }  else if (bean.getStatue().equals(RechargeMessage.AWARD_TYPE)){
                bean.setId(IdUtils.getId());
                bean.setOrderid(body.getAllin_trade_no());
                bean.setStatue(RechargeMessage.AWARD_TYPE);
                bean.setAmount(body.getMoney());
                bean.setProductid(body.getProduct_id());
                bean.setUpdateat(new Date());
                bean.setCreateat(new Date());
                dataInfoMapper.insertSelective(bean);
                userSubscribe.setId(String.valueOf(bean.getId()));
                userSubscribe.setSubscribeduetime(subscribe_due_time);
                userSubscribeDao.updateByPrimaryKeySelective(userSubscribe);
                return "success";
            }
            else {
                return "fail";
            }
        } catch (Exception e) {
            logger.error("验证订阅订单解析失败：" + JSON.toJSONString(body) + e.getMessage(), e);
            return "fail";
        }
    }

    @Override
    public Long getSBDueTime(String roleId, String productId) {
        UserSubscribeKey userSubscribeKey=new UserSubscribeKey();
        userSubscribeKey.setUserId(roleId);
        userSubscribeKey.setProductId(productId);
        UserSubscribe userSubscribe = userSubscribeDao.selectByPrimaryKey(userSubscribeKey);
        if (userSubscribe==null){
            return 0L;
        }
        return userSubscribe.getSubscribeduetime();
    }


}
