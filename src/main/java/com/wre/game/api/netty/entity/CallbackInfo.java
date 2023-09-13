package com.wre.game.api.netty.entity;

/**
 * 充值回调的参数配置
 * @author sxw
 */
public class CallbackInfo {
    private String md5Key;
    private String callbackKey;

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public String getCallbackKey() {
        return callbackKey;
    }

    public void setCallbackKey(String callbackKey) {
        this.callbackKey = callbackKey;
    }

    @Override
    public String toString() {
        return "CallbackInfo{" +
                "md5Key='" + md5Key + '\'' +
                ", callbackKey='" + callbackKey + '\'' +
                '}';
    }
}
