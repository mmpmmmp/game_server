package com.wre.game.api.data.param;

public class HwRechargeCostParam {
    private String purchaseToken;
    private String productId;



    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    @Override
    public String toString() {
        return "HwRechargeCostParam{" +
                "purchaseToken='" + purchaseToken + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
