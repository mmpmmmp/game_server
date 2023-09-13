package com.wre.game.api.data.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class PaymentTransaction {
    private Long id;
    private String appId;
    private String appName;
    private String productId;
    private String transactionId;
    private String statusCode;
    private Date updatedAt;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentTransactionData{");
        sb.append("id=").append(id);
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", productId='").append(productId).append('\'');
        sb.append(", transactionId='").append(transactionId).append('\'');
        sb.append(", statusCode='").append(statusCode).append('\'');
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
