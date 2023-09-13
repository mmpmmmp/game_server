package com.wre.game.api.entity;

public class KSRechargeReq {

    private String dataString;

    private String sign;

    @Override
    public String toString() {
        return "KSRechargeReq{" +
                "dataString='" + dataString + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
