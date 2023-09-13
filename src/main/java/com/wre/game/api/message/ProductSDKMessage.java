package com.wre.game.api.message;

/**
 * productSDK 参数管理
 *
 * @author sxw
 */
public class ProductSDKMessage {

    public static final String NOTIFY_URL="https://test/gameapi/v1/payment/verifyForKS";
    public static final String SUBSCRIBE_NOTYFY_URL="https://test/gameapi/v1/payment/verifySubForKS";
    /**
     * 渠道15471 参数配置
     */
    public static class YiGuan {
        public static final String PLATFORM="Android";

        public static final String client_id="yiguan";

        public static final String client_secret="yiguan";

        public static final String app_id="yiguan_gp";

        public static final String publicKey="key";
    }


    public static class Cooking{
        public static final String publicKey="key";
        public static final String app_id="app_id";

    }

    public static class AssemblyLine{
        public static final String ks_appId="ks_appId";
        public static final String app_id="app_id";
        public static final String appPublicKey="key";
        public static final String publicKey="key";
        public static final String HW_CLIENT_SECRET="HW_CLIENT_SECRET";
        public static final String HW_CLIENT_ID="HW_CLIENT_ID";
        public static final String HW_ROOT_URL="https://test";
        public static final String HW_PUBLIC_KEY="HW_PUBLIC_KEY";
    }

    /**
     * 挖矿项目
     * */
    public static class Mining{
        public static final Integer appId=1;
        public static final String appKey="appKey";
        public static final String appSecret="appSecret";
    }

    public static class ResultCode{
        public static final Integer ResultSuccess=1;
        public static final Integer ResultFail=0;
        public static final Integer ResultVerify=91;
    }

}
