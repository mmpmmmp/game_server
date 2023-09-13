package com.wre.game.api.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @JsonIgnore
    private Long userId;
    private String uuid;
    @JsonIgnore
    private String openId;
    @JsonIgnore
    private String anonymousOpenId;
    @JsonIgnore
    private String unionId;
    private String nickName;
    private String gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private Integer block;
    private String groupType;
    private String shareType;
    @JsonIgnore
    private String shareFromUuid;
    private String shareFromImage;
    private String sceneId;
    @JsonIgnore
    private String channel;
    @JsonIgnore
    private String appId;
    @JsonIgnore
    private String appName;
    @JsonIgnore
    private String appVersion;
    @JsonIgnore
    private Long lastLoginTime;
    @JsonIgnore
    private Long updatedAt;


    private Date createdAt;

    private String sessionKey;

    private String channelId;

    private String channelName;

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", uuid='" + uuid + '\'' +
                ", openId='" + openId + '\'' +
                ", anonymousOpenId='" + anonymousOpenId + '\'' +
                ", unionId='" + unionId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", block=" + block +
                ", shareType='" + shareType + '\'' +
                ", shareFromUuid='" + shareFromUuid + '\'' +
                ", shareFromImage='" + shareFromImage + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", channel='" + channel + '\'' +
                ", appId='" + appId + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", updatedAt=" + updatedAt +
                ", createdAt=" + createdAt +
                ", sessionKey='" + sessionKey + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelName='" + channelName + '\'' +
                '}';
    }
    
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getUuid() {
        return uuid;
    }

    public String getAnonymousOpenId() {
        return anonymousOpenId;
    }

    public void setAnonymousOpenId(String anonymousOpenId) {
        this.anonymousOpenId = anonymousOpenId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getShareFromUuid() {
        return shareFromUuid;
    }

    public void setShareFromUuid(String shareFromUuid) {
        this.shareFromUuid = shareFromUuid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getShareFromImage() {
        return shareFromImage;
    }

    public void setShareFromImage(String shareFromImage) {
        this.shareFromImage = shareFromImage;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

}
