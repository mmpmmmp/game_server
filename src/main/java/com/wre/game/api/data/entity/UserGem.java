package com.wre.game.api.data.entity;

import java.util.Date;

public class UserGem {

    private Long id;
    private Long userId;
    private String appId;
    private Long gem;
    private Date updatedAt;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getGem() {
        return gem;
    }

    public void setGem(Long gem) {
        this.gem = gem;
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
        return "UserGem{" +
                "id=" + id +
                ", userId=" + userId +
                ", appId='" + appId + '\'' +
                ", gem=" + gem +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                '}';
    }
}
