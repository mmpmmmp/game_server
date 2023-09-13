package com.wre.game.api.data.entity;

public class UserActionShare {
    private Long id;
    private Long userId;
    private String shareType;
    private String channel;
    private String appId;
    private String appName;
    private String imageId;
    private Long createdAt;

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

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserActionShare{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", shareType='").append(shareType).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
