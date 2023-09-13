package com.wre.game.api.data.param;


public class PaymentReqParam {
    /**
     * @param data.productId     产品ID
     * @param data.transactionId 苹果内购交易ID
     * @param data.payload       校验体（base64字符串）
     */
    private String transactionId;

    private String payload;
    /**流水号*/
    private String serialId;

    @Override
    public String toString() {
        return "PaymentReqParam{" +
                "transactionId='" + transactionId + '\'' +
                ", payload='" + payload + '\'' +
                ", serialId='" + serialId + '\'' +
                '}';
    }

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }


}
