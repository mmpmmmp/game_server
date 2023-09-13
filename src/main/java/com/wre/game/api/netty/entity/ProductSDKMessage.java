package com.wre.game.api.netty.entity;

/** productSDK 参数管理
 * @author sxw
 * */
public class ProductSDKMessage {

    /** 渠道1 参数配置*/
    public static class Ios_1{
        public final static String PLATFORM="1";
        public final static String URL = "http://test/webapi/checkUserInfo";
        public final static String PRODUCT_CODE = "code";
        public final static String MD5_KEY = "key";
        public final static String CALLBACK_KEY = "key";
    }
    /** 渠道2 参数配置*/
    public static class Android_32{
        public final static String PLATFORM="2";
        public final static String URL = "http://test/v2/checkUserInfo";
    	public final static String PRODUCT_CODE = "code";
        public final static String MD5_KEY = "key";
     	public final static String CALLBACK_KEY = "key";
     	public final static String IS_STATE="1";
    }
    /** 渠道3 参数配置*/
    public static class Android_3{
        public final static String PLATFORM="3";
        public final static String URL = "http://test/v2/checkUserInfo";
        public final static String PRODUCT_CODE = "code";
        public final static String MD5_KEY = "key";
        public final static String CALLBACK_KEY = "key";
        public final static String IS_STATE="1";
    }

    /** 渠道4 参数配置*/
    public static class Android_4{
        public final static String PLATFORM="4";
        public final static String URL = "http://test/v2/checkUserInfo";
        public final static String PRODUCT_CODE = "code";
        public final static String MD5_KEY = "key";
        public final static String CALLBACK_KEY = "key";
        public final static String IS_STATE="1";
    }




}
