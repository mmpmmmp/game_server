package com.wre.game.api.data.param;


import org.apache.commons.lang3.builder.ToStringBuilder;

public class DataReqParam {
    private String uuid;
    private String appId;
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    private Integer score;

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

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uuid", uuid)
                .append("appId", appId)
                .append("param1", param1)
                .append("param2", param2)
                .append("param3", param3)
                .append("param4", param4)
                .append("param5", param5)
                .append("score", score)
                .toString();
    }
}
