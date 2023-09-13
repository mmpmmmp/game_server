package com.wre.game.api.entity;

public class RechargeCreateInfo {
    /**
     * 流水订单号
     */
    private String serialId;

    @Override
    public String toString() {
        return "RechargeCreateInfo{" +
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
