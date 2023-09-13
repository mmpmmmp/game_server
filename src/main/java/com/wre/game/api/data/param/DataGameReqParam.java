package com.wre.game.api.data.param;


public class DataGameReqParam {
    private String uuid;
    private String appId;
    private String data;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DataGameReqParam{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
