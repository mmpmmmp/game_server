package com.wre.game.api.data.param;


public class UserActionGameReqParam {
    private String toAppId;
    private String toAppName;

    public String getToAppId() {
        return toAppId;
    }

    public void setToAppId(String toAppId) {
        this.toAppId = toAppId;
    }

    public String getToAppName() {
        return toAppName;
    }

    public void setToAppName(String toAppName) {
        this.toAppName = toAppName;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserActionGameReqParam{");
        sb.append(", toAppId='").append(toAppId).append('\'');
        sb.append(", toAppName='").append(toAppName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
