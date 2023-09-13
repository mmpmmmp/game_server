package com.wre.game.api.service;

import com.wre.game.api.constants.*;
import com.wre.game.api.data.param.AndroidReqParam;
import com.wre.game.api.data.param.HwRechargeCostParam;
import com.wre.game.api.data.param.PaymentReqParam;
import com.wre.game.api.data.param.RechargeCostParam;
import com.wre.game.api.entity.HWRecahrgeParam;
import com.wre.game.api.entity.KSRechargeParam;

public interface PaymentService {
    public ApiResponseDataMessage payIOS(PaymentReqParam data);

    /**
     * 安卓支付
     *
     * @param body
     * @return
     */
    public ApiResponseDataMessage payAndroid(AndroidReqParam body, String appId);

    /**
     * 消耗
     *
     * @param body
     * @return
     */
    public ApiResponseDataMessage cost(RechargeCostParam body);

    /**
     * 消耗
     *
     * @param body
     * @return
     */
    public ApiResponseDataMessage costSdk(RechargeCostParam body);

    /**
     * 查询补单
     * @param userId
     * @return
     */
    public ApiResponseDataMessage add(String userId);


    /**
     * sdk的回调验证
     *
     * @return
     */
    public SdkResponseDataMessage sdkVerify(String AppID,
                                            String OrderSerial,
                                            String CooperatorOrderSerial,
                                            String Sign,
                                            String Content);



    /**快手的sdk验证*/
    public ApiResponseDataMessage createRechargeByKuaishou(KSRechargeParam body,String appId,String channel);

    /**快手sdk的验证*/
    public String verifyForKS(String app_id,
                              String role_id,
                              String server_id,
                              String money,
                              String product_id,
                              String extension,
                              String allin_trade_no,
                              String data,
                              String sign);

    ApiResponseDataMessage vertifyForHW(HWRecahrgeParam body);

    ApiResponseDataMessage costByHw(HwRechargeCostParam body);

    String verifySubForKS(String app_id, String role_id, String server_id, String money, String product_id, String notify_detail,String extension, String allin_trade_no, String data, String sign);

    Long getSBDueTime(String roleId, String productId);
}