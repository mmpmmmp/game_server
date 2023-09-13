package com.wre.game.api.data.param;


import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class UserLoginReqParam implements Serializable {

    private static final long serialVersionUID = 2638192749705821901L;
    private String code;
    private String anonymousCode; //toutiao code

    @Override
    public String toString() {
        return "UserLoginReqParam{" +
                "code='" + code + '\'' +
                ", anonymousCode='" + anonymousCode + '\'' +
                ", shareType='" + shareType + '\'' +
                ", shareFromOpenId='" + shareFromOpenId + '\'' +
                ", shareFromUuid='" + shareFromUuid + '\'' +
                ", shareFromImage='" + shareFromImage + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", channelName='" + channelName + '\'' +
                '}';
    }

    /**
     * 玩家打开游戏的方式,暂定的是0是自己主动打开游戏，
     * 1普通个人分享，
     * 2是普通群分享 ，
     * 3表示从别的游戏跳转，
     * 10-20功能性分享（暂时预留10		个）
     */
    private String shareType;
    private String shareFromOpenId;
    private String shareFromUuid;
    private String shareFromImage;
    private String sceneId;
    private String channelId;
    private String channelName;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnonymousCode() {
        return anonymousCode;
    }

    public void setAnonymousCode(String anonymousCode) {
        this.anonymousCode = anonymousCode;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getShareFromOpenId() {
        return shareFromOpenId;
    }

    public void setShareFromOpenId(String shareFromOpenId) {
        this.shareFromOpenId = shareFromOpenId;
    }

    public String getShareFromUuid() {
        return shareFromUuid;
    }

    public void setShareFromUuid(String shareFromUuid) {
        this.shareFromUuid = shareFromUuid;
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
