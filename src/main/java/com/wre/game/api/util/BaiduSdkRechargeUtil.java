package com.wre.game.api.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.wre.game.api.data.param.VerifySdkBaidu;
import com.wre.game.api.message.ProductSDKMessage;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author admin
 * 对接百度sdk的方法
 */
public class BaiduSdkRechargeUtil {

    /**
     * 返回Content信息
     * @param json
     * @return
     */
    public static VerifySdkBaidu.Content getContent(String json){
        com.wre.game.api.data.param.VerifySdkBaidu.Content content= JSONUtil.toBean(Base64.decodeStr(json), com.wre.game.api.data.param.VerifySdkBaidu.Content.class);
        return content;
    }

    /**
     * 整合信息
     */
    public static VerifySdkBaidu getData(String AppID,
                                  String OrderSerial, String CooperatorOrderSerial,String Sign, String Content ){
        VerifySdkBaidu result=new VerifySdkBaidu();
        result.setAppId(Fn.toInt(AppID));
        result.setOrderSerial(OrderSerial);
        result.setCooperatorOrderSerial(CooperatorOrderSerial);
        result.setSign(Sign);
        result.setContent(getContent(Content));
        return result;
    }

    /**
     * md5签名验证
     */
    public static boolean md5Sign(String AppID,
                           String OrderSerial, String CooperatorOrderSerial,String Sign, String Content ){
        String md5Str=AppID+OrderSerial+CooperatorOrderSerial+Content+ProductSDKMessage.Mining.appSecret;
        String md5= SecureUtil.md5(md5Str);
        if(md5.equals(Sign)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 返回的md5验证签名
     * @param appId
     * @param resultCode
     * @return
     */
    public  static String md5SignResult(String appId,String resultCode){
        String md5Str=appId+resultCode+ProductSDKMessage.Mining.appSecret;
        return SecureUtil.md5(md5Str);
    }

    public static void main(String[] args) {
     System.out.println(Base64.decodeStr("aa"));
    }



}
