package com.wre.game.api.data.param;

public class AndroidReqParam {

    private String serialId;
    private String dataJson;
    private String sign;

    @Override
    public String toString() {
        return "AndroidReqParam{" +
                "serialId='" + serialId + '\'' +
                ", dataJson='" + dataJson + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
