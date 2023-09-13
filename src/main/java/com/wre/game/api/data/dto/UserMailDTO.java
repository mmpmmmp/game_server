package com.wre.game.api.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserMailDTO {
    private Integer mailId;
    private Integer type;
    private String title;
    private String content;
    @JsonIgnore
    private String fromUserName;
    private String awards;
    private Integer startTime;
    private Integer endTime;
    private Integer state;
    private String toUserId;
    @JsonIgnore
    private Integer hasInsert;

    public Integer getHasInsert() {
        return hasInsert;
    }

    public void setHasInsert(Integer hasInsert) {
        this.hasInsert = hasInsert;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getMailId() {
        return mailId;
    }

    public void setMailId(Integer mailId) {
        this.mailId = mailId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
