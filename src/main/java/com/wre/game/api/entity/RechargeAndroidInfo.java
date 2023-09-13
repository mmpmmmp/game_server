package com.wre.game.api.entity;

/**
 * sdk告诉我的充值订单
 *
 * @author zs
 * @date 2020-03-27 14:20:21
 */
public class RechargeAndroidInfo {
    /**订单的验证参数*/
    private String dataJson;
    /**订单状态*/
    private int purchaseState;
    // 为玩家充值的商品Id
    private String productId;
    /**订单签名*/
    private String sign;
    /**ordid*/
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String obfuscatedAccountId;
    /**订单token*/
    private String purchaseToken;

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    /**订单id*/
    private String obfuscatedProfileId;

    public String getObfuscatedAccountId() {
        return obfuscatedAccountId;
    }

    public void setObfuscatedAccountId(String obfuscatedAccountId) {
        this.obfuscatedAccountId = obfuscatedAccountId;
    }

    public String getDataJson() {
        return dataJson;
    }


    public String getObfuscatedProfileId() {
        return obfuscatedProfileId;
    }

    public void setObfuscatedProfileId(String obfuscatedProfileId) {
        this.obfuscatedProfileId = obfuscatedProfileId;
    }

    /**
     * 包名
     * @return
     */
    private String packageName;
    /**
     * 充值时间
     * @return
     */
    private String purchaseTime;


    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public int getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(int purchaseState) {
        this.purchaseState = purchaseState;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    @Override
    public String toString() {
        return "RechargeAndroidInfo{" +
                "dataJson='" + dataJson + '\'' +
                ", purchaseState=" + purchaseState +
                ", productId='" + productId + '\'' +
                ", sign='" + sign + '\'' +
                ", orderId='" + orderId + '\'' +
                ", obfuscatedAccountId='" + obfuscatedAccountId + '\'' +
                ", purchaseToken='" + purchaseToken + '\'' +
                ", obfuscatedProfileId='" + obfuscatedProfileId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", purchaseTime='" + purchaseTime + '\'' +
                '}';
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
