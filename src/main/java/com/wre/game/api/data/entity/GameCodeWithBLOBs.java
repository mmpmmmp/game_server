package com.wre.game.api.data.entity;

import java.util.Date;

public class GameCodeWithBLOBs extends GameCode {
    private String awards;

    private String roleids;

    public GameCodeWithBLOBs(Integer id, String code, String startTime, String endTime, Date createTime, Integer maxCount, Integer useCount, Date updateTime, String awards, String roleids) {
        super(id, code, startTime, endTime, createTime, maxCount, useCount, updateTime);
        this.awards = awards;
        this.roleids = roleids;
    }

    public GameCodeWithBLOBs() {
        super();
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards == null ? null : awards.trim();
    }

    public String getRoleids() {
        return roleids;
    }

    public void setRoleids(String roleids) {
        this.roleids = roleids == null ? null : roleids.trim();
    }
}