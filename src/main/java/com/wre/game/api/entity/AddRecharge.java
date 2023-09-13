package com.wre.game.api.entity;

public class AddRecharge {

    /**
     * 流水订单号
     */
    private String serialId;

    /**
     * 礼包id
     */
    private String productId;

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    @Override
    public String toString() {
        return "AddRecharge{" +
                "serialId='" + serialId + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
