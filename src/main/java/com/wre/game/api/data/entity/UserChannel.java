package com.wre.game.api.data.entity;

public class UserChannel {
    private Integer id;

    private String userUuid;

    private String userAppId;

    private String userChannel;

    private String userChannelId;

    public UserChannel(Integer id, String userUuid, String userAppId, String userChannel, String userChannelId) {
        this.id = id;
        this.userUuid = userUuid;
        this.userAppId = userAppId;
        this.userChannel = userChannel;
        this.userChannelId = userChannelId;
    }

    public UserChannel() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid == null ? null : userUuid.trim();
    }

    public String getUserAppId() {
        return userAppId;
    }

    public void setUserAppId(String userAppId) {
        this.userAppId = userAppId == null ? null : userAppId.trim();
    }

    public String getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(String userChannel) {
        this.userChannel = userChannel == null ? null : userChannel.trim();
    }

    public String getUserChannelId() {
        return userChannelId;
    }

    public void setUserChannelId(String userChannelId) {
        this.userChannelId = userChannelId == null ? null : userChannelId.trim();
    }
}