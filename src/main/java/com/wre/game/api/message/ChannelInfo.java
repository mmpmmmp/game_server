package com.wre.game.api.message;

/**
 * 服务于渠道信息
 * @author sxw
 */
public class ChannelInfo {
    /**
     * 渠道信息的code值
     */
    public static  class  ChannelCode{
        public  static final  String   WE_CHAT ="wechat";
        public  static final  String   DOU_YIN ="douyin";
        public  static final  String   QQ_MINI ="qqmini";
        public  static final  String   S_WAN ="swan";
        public  static final  String   TOU_TIAO="toutiao";
        public static  final  String   FACE_BOOK="facebook";
        /**动物医院专用*/
        public static  final  String   TAP="tap";
        public static  final  String   DEFAULT_CHANNEL="default";
    }

}
