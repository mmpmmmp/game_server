package com.wre.game.api.data.entity;

import java.io.Serializable;

/**
 * user_subscribe
 * @author 
 */
public class UserSubscribeKey implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 商品id
     */
    private String productId;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}