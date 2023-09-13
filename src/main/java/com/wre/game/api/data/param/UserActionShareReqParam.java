package com.wre.game.api.data.param;


public class UserActionShareReqParam {
    private String shareType;
    private String imageId;

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserActionShareReqParam{");
        sb.append("shareType='").append(shareType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
