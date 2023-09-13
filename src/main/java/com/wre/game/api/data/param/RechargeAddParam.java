package com.wre.game.api.data.param;

public class RechargeAddParam {

    private String userId;

    private String uuid;

    @Override
    public String toString() {
        return "RechargeAddParam{" +
                "userId='" + userId + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
