package com.wre.game.api.data.param;

public class MailChangeParam {
    private int mailId;
    private int state;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getMailId() {
        return mailId;
    }

    public void setMailId(int mailId) {
        this.mailId = mailId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "MailChangeParam{" +
                "mailId=" + mailId +
                ", state=" + state +
                ", userId='" + userId + '\'' +
                '}';
    }
}
