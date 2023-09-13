package com.wre.game.api.data.entity;

import java.util.Date;

public class GameCode {
    private Integer id;

    private String code;

    private String startTime;

    private String endTime;

    private Date createTime;

    private Integer maxCount;

    private Integer useCount;

    private Date updateTime;

    public GameCode(Integer id, String code, String startTime, String endTime, Date createTime, Integer maxCount, Integer useCount, Date updateTime) {
        this.id = id;
        this.code = code;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createTime = createTime;
        this.maxCount = maxCount;
        this.useCount = useCount;
        this.updateTime = updateTime;
    }

    public GameCode() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime == null ? null : startTime.trim();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime == null ? null : endTime.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}