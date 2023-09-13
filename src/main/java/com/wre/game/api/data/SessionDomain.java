package com.wre.game.api.data;


import com.wre.game.api.constants.Channel;

public class SessionDomain {
    // Javascript Data type Number != Java Long type. so use uid as String
    private String userId;
    private String block;
    private String channel;
    private String uuid;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SessionDomain{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", block='").append(block).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", uuid='").append(uuid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
