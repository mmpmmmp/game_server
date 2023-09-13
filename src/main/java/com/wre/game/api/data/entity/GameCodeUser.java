package com.wre.game.api.data.entity;

import java.util.Date;

public class GameCodeUser {
    private Integer id;

    private String userId;

    private Integer codeId;

    private Date useTime;

    public Date getUseTime() {
        return useTime;
    }

    public void setUseTime(Date useTime) {
        this.useTime = useTime;
    }

    public GameCodeUser(Integer id, String userId, Integer codeId) {
        this.id = id;
        this.userId = userId;
        this.codeId = codeId;
    }

    public GameCodeUser() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }
}