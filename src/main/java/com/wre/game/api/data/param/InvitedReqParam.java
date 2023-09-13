package com.wre.game.api.data.param;

import java.util.List;

public class InvitedReqParam {

    String toUUid;
    String shareType;
    List<FBInvitedReqParam> body;
    String uuid;

    public List<FBInvitedReqParam> getBody() {
        return body;
    }

    public void setBody(List<FBInvitedReqParam> body) {
        this.body = body;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToUUid() {
        return toUUid;
    }

    public void setToUUid(String toUUid) {
        this.toUUid = toUUid;
    }


    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    @Override
    public String toString() {
        return "InvitedReqParam{" +
                "toUUid='" + toUUid + '\'' +
                ", shareType='" + shareType + '\'' +
                ", body=" + body +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
