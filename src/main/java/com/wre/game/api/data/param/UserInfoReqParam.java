package com.wre.game.api.data.param;


import java.io.Serializable;

public class UserInfoReqParam implements Serializable {

    private static final long serialVersionUID = 7472662924060296231L;
    private String nickName;
    private String gender;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserInfoReqParam{");
        sb.append("nickName='").append(nickName).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", province='").append(province).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", avatarUrl='").append(avatarUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
