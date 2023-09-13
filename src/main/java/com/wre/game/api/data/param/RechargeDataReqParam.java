package com.wre.game.api.data.param;

public class RechargeDataReqParam {
    private String amount;
    private String productId;
    private String userId;
    private String afId;
    private String type;//android ios

    @Override
    public String toString() {
        return "RechargeDataReqParam{" +
                "amount='" + amount + '\'' +
                ", productId='" + productId + '\'' +
                ", userId='" + userId + '\'' +
                ", afId='" + afId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAfId() {
        return afId;
    }

    public void setAfId(String afId) {
        this.afId = afId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
