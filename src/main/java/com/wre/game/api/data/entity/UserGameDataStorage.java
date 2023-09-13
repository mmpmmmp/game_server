package com.wre.game.api.data.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class UserGameDataStorage {
    private Long id;
    private String uuid;
    private String appId;
    private String appName;
    private String dataStorage;
    private Date updatedAt;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDataStorage() {
        return dataStorage;
    }

    public void setDataStorage(String dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserGameDataStorage{");
        sb.append("id=").append(id);
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", dataStorage='").append(dataStorage).append('\'');
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
