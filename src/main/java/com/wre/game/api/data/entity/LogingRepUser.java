package com.wre.game.api.data.entity;

import com.wre.game.api.constants.ApiHeader;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 登录参数注入类
 * @author admin
 */
public class LogingRepUser {
    /**
     * 游戏项目id
     */
    private String appId;
    /**
     * 游戏名称
     */
    private String appName;
    /**
     * 游戏版本号
     */
    private String version;
    /**
     * 客户端ip
     */
    private String clientIpAddr;
    /**
     * 渠道
     */
    private String channel;
    /**
     * token
     */
    private String token;
    /**
     * 分享id
     */
    private String shareId;
    /**
     * 渠道id
     */
    private String channelId;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getClientIpAddr() {
        return clientIpAddr;
    }

    public void setClientIpAddr(String clientIpAddr) {
        this.clientIpAddr = clientIpAddr;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LogingRepUser{" +
                "appId='" + appId + '\'' +
                ", appName='" + appName + '\'' +
                ", version='" + version + '\'' +
                ", clientIpAddr='" + clientIpAddr + '\'' +
                ", channel='" + channel + '\'' +
                ", token='" + token + '\'' +
                ", shareId='" + shareId + '\'' +
                '}';
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }
}
