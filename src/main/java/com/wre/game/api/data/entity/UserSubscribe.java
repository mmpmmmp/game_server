package com.wre.game.api.data.entity;

import java.io.Serializable;

/**
 * user_subscribe
 * @author 
 */
public class UserSubscribe extends UserSubscribeKey implements Serializable {
    /**
     * 流水号
     */
    private String id;

    private String appId;

    /**
     * 到期时间戳
     */
    private Long subscribeduetime=0L;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getSubscribeduetime() {
        return subscribeduetime;
    }

    public void setSubscribeduetime(Long subscribeduetime) {
        this.subscribeduetime = subscribeduetime;
    }
}