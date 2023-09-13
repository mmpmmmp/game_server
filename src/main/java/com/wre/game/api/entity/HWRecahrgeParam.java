package com.wre.game.api.entity;

public class HWRecahrgeParam {

    private String body;
    private String sign;
    private String channel;
    private String purchaseToken;
    private String AppId;
    private String userId;
    private String amount;
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }


    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "HWRecahrgeParam{" +
                "body='" + body + '\'' +
                ", sign='" + sign + '\'' +
                ", channel='" + channel + '\'' +
                ", purchaseToken='" + purchaseToken + '\'' +
                ", AppId='" + AppId + '\'' +
                ", userId='" + userId + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
