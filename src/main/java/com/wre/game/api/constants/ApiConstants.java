package com.wre.game.api.constants;

public interface ApiConstants {

    // [DataSource Constants]

    // [Common Constants]
    public static final String TOKEN_HASH_KEY_USER_ID = "userId";
    public static final String TOKEN_HASH_KEY_OLD = "old";

    // [Common Delimiter String]
    public static final String DELIMITER_UNDERSCORE = "_";
    public static final String DELIMITER_HYPHEN = "-";
    public static final String DELIMITER_SLASH = "/";
    public static final String DELIMITER_COLON = ":";
    public static final String DELIMITER_COMMA = ",";
    public static final String DELIMITER_DOT = ".";


    // [Common Sort Prefix String]
    public static final String SORT_ASCENDING = "+";
    public static final String SORT_DESCENDING = "-";

    public static final String RETENTION_NEW = "new";
    public static final String RETENTION_ACTIVE = "active";

    // [Common Cookies Names]

    // [USER table contants]

    // Wechat Keys
//    public static final HashMap<String, String> WECHAT_KEYS = new HashMap<String, String>() {
//        {
//            put("wx4e0f107160341efc", "0c9fdaff5bcd87d8de89ae493c4bdda2");
//            put("wx2651f491c780dfab", "c9313a771b1b874ce5c698102622a260");
//            put("wx28756efa9d3c411f", "51e91c929069dcaa7099b0da0a575cef");
//            put("wxcf0f81b4d40666c4", "fd3717607ba0a099f4c31523878adee1");
//        }
//    };

    public static final String WECHAT_CONTENT="你好，点击下方二维码，长按识别，可加入互助群，有什么问题也可以找群内客服小姐姐，谢谢~";
    public static final String WECHAT_CONTENT_GROUP_NOT_AVAILABLE="你好，暂无可用群组~";
    interface WechatMessageType {
        String MINI_PROGRAM_PAGE = "miniprogrampage";
        String IMAGE = "image";
        String TEXT = "text";
        String EVENT = "event";
    }
}
