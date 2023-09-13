package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class WechatException extends ApiException {

    private static final long serialVersionUID = 7555761672655956091L;
    private String wechatErrorMessage = "";

    public String getWechatErrorMessage() {
        return wechatErrorMessage;
    }

    public void setWechatErrorMessage(String wechatErrorMessage) {
        this.wechatErrorMessage = wechatErrorMessage;
    }

    public WechatException() {
        super();
    }

    public WechatException(RtCode rtCode) {
        super(rtCode);
    }

    public WechatException(RtCode rtCode, Throwable throwable) {
        super(rtCode, throwable);
    }

    public WechatException(RtCode rtCode, String wechatErrorMessage) {
        super(rtCode);
        this.wechatErrorMessage = wechatErrorMessage;
    }
}
