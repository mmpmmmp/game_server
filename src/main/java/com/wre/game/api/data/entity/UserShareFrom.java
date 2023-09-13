package com.wre.game.api.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

public class UserShareFrom {
    private Long id;
    private String fromUuid;
    private String toUuid;
    private String shareType;
    private String appId;
    private String appName;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFromUuid() {
        return fromUuid;
    }

    public void setFromUuid(String fromUuid) {
        this.fromUuid = fromUuid;
    }

    public String getToUuid() {
        return toUuid;
    }

    public void setToUuid(String toUuid) {
        this.toUuid = toUuid;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserShareFrom{");
        sb.append("id=").append(id);
        sb.append(", fromUuid='").append(fromUuid).append('\'');
        sb.append(", toUuid='").append(toUuid).append('\'');
        sb.append(", shareType='").append(shareType).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
