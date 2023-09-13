package com.wre.game.api.data.param;


public class SendGroupReqParam {
    private String openId;
    private String appId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SendGroupReqParam{");
        sb.append("openId='").append(openId).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
