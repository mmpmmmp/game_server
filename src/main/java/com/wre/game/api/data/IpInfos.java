package com.wre.game.api.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.List;

public class IpInfos implements Serializable {
    private static final long serialVersionUID = -2392543324513405498L;
    private List<IpSection> blockIpList;

    public List<IpSection> getBlockIpList() {
        return blockIpList;
    }

    public void setBlockIpList(List<IpSection> blockIpList) {
        this.blockIpList = blockIpList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("blockIpList", blockIpList)
                .toString();
    }
}
