package com.wre.game.api.data;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private static final long serialVersionUID = 8269459797727406577L;
    private String appId;

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    private String appSecret;
    private String appName;
    private String serverVersion;
    private String publicKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
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

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GameInfo{");
        sb.append("appId='").append(appId).append('\'');
        sb.append(", appSecret='").append(appSecret).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", serverVersion='").append(serverVersion).append('\'');
        sb.append(", publicKey='").append(publicKey).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
