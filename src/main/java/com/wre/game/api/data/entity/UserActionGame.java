package com.wre.game.api.data.entity;

public class UserActionGame {
    private Long id;
    private Long userId;
    private String fromAppId;
    private String fromAppName;
    private String toAppId ;
    private String toAppName;
    private String channel;
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

    public String getFromAppId() {
        return fromAppId;
    }

    public void setFromAppId(String fromAppId) {
        this.fromAppId = fromAppId;
    }

    public String getFromAppName() {
        return fromAppName;
    }

    public void setFromAppName(String fromAppName) {
        this.fromAppName = fromAppName;
    }

    public String getToAppId() {
        return toAppId;
    }

    public void setToAppId(String toAppId) {
        this.toAppId = toAppId;
    }

    public String getToAppName() {
        return toAppName;
    }

    public void setToAppName(String toAppName) {
        this.toAppName = toAppName;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserActionGame{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", fromAppId='").append(fromAppId).append('\'');
        sb.append(", fromAppName='").append(fromAppName).append('\'');
        sb.append(", toAppId='").append(toAppId).append('\'');
        sb.append(", toAppName='").append(toAppName).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
