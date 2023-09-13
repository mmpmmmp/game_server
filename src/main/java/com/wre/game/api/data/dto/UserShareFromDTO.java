package com.wre.game.api.data.dto;

import java.util.Date;

/**
 *
 */
public class UserShareFromDTO {


    private String uuid = "";

    private String nickName = "";

    private String avatarUrl = "";

    private String country = "";

    private String province = "";

    private String city = "";

    private String gender = "";

    private Date createdAt;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserShareFromDTO{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", nickName='").append(nickName).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append(", country=").append(country);
        sb.append(", province=").append(province);
        sb.append(", city='").append(city).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append('}');
        return sb.toString();
    }
}
