package com.wre.game.api.data.entity;

public class UserActionAd {
    private Long id;
    private Long userId;
    private String adType;
    private String adPosition;
    private String appId;
    private String channel;
    private String appName;
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

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(String adPosition) {
        this.adPosition = adPosition;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserActionAd{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", adType='").append(adType).append('\'');
        sb.append(", adPosition='").append(adPosition).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
