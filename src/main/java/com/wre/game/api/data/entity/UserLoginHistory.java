package com.wre.game.api.data.entity;

import java.util.Date;

public class UserLoginHistory {

    private Long id;
    private Long userId;
    private String uuid;
    private String channel;
    private String appId;
    private String appName;
    private String appVersion;
    private String newUserYn;
    private Date userCreatedAt;
    private Date loginTime;
    private String groupType;

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Date getUserCreatedAt() {
        return userCreatedAt;
    }

    public void setUserCreatedAt(Date userCreatedAt) {
        this.userCreatedAt = userCreatedAt;
    }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getNewUserYn() {
        return newUserYn;
    }

    public void setNewUserYn(String newUserYn) {
        this.newUserYn = newUserYn;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserLoginHistory{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", appVersion='").append(appVersion).append('\'');
        sb.append(", newUserYn='").append(newUserYn).append('\'');
        sb.append(", userCreatedAt=").append(userCreatedAt);
        sb.append(", loginTime=").append(loginTime);
        sb.append('}');
        return sb.toString();
    }
}
