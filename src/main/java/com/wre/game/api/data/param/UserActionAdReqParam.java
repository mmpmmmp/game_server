package com.wre.game.api.data.param;


public class UserActionAdReqParam {
    private String adType;
    private String adPosition;

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdPosition() {
        return adPosition;
    }

    public void setAdPosition(String adPosition) {
        this.adPosition = adPosition;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserActionAdReqParam{");
        sb.append("adType='").append(adType).append('\'');
        sb.append(", adPosition='").append(adPosition).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
