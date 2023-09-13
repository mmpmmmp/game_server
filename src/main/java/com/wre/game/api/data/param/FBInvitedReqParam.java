package com.wre.game.api.data.param;

public class FBInvitedReqParam {
    private String userId;
    private String sharePos;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSharePos() {
        return sharePos;
    }

    public void setSharePos(String sharePos) {
        this.sharePos = sharePos;
    }

    public FBInvitedReqParam() {
    }

    public FBInvitedReqParam(String userId, String sharePos) {
        this.userId = userId;
        this.sharePos = sharePos;
    }

    @Override
    public String toString() {
        return "FBInvitedReqParam{" +
                "userId='" + userId + '\'' +
                ", sharePos='" + sharePos + '\'' +
                '}';
    }
}
