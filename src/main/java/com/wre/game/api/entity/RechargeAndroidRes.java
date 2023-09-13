package com.wre.game.api.entity;

public class RechargeAndroidRes {

    private Integer state;

    private String environment="";

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public Integer getState() {
        return state;
    }

    public RechargeAndroidRes(Integer state) {
        this.state = state;
    }

    public RechargeAndroidRes(Integer state, String environment) {
        this.state = state;
        this.environment = environment;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "RechargeAndroidRes{" +
                "state=" + state +
                '}';
    }
}
