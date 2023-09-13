package com.wre.game.api.data.dto;

import java.util.Date;

/**
 *
 */
public class UserRankDTO {

    private Integer rank;
    private Long id;
    private String uuid;
    private String openId;
    private String gender;
    private String nickName;
    private String avatarUrl;

    private String appId;
    private String appName;

    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    private Integer score;
    private Date dataUpdatedAt;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public Date getDataUpdatedAt() {
        return dataUpdatedAt;
    }

    public void setDataUpdatedAt(Date dataUpdatedAt) {
        this.dataUpdatedAt = dataUpdatedAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserRankDTO{");
        sb.append("id=").append(id);
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append(", openId='").append(openId).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append(", appId='").append(appId).append('\'');
        sb.append(", appName='").append(appName).append('\'');
        sb.append(", param1=").append(param1);
        sb.append(", param2=").append(param2);
        sb.append(", param3=").append(param3);
        sb.append(", param4=").append(param4);
        sb.append(", param5=").append(param5);
        sb.append(", dataUpdatedAt=").append(dataUpdatedAt);
        sb.append('}');
        return sb.toString();
    }
}
