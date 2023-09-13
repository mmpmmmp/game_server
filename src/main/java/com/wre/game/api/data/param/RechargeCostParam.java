package com.wre.game.api.data.param;

public class RechargeCostParam {

    private String serialId;

    @Override
    public String toString() {
        return "RechargeCostParam{" +
                "serialId='" + serialId + '\'' +
                '}';
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }
}
