package com.wre.game.api.data;

public class IpBlock {
    private boolean isBlock;
    private String ip;
    private String blockRegion = "";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public String getBlockRegion() {
        return blockRegion;
    }

    public void setBlockRegion(String blockRegion) {
        this.blockRegion = blockRegion;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("IpBlock{");
        sb.append("isBlock=").append(isBlock);
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", blockRegion='").append(blockRegion).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
