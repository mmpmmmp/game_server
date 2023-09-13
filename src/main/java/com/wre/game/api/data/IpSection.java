package com.wre.game.api.data;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class IpSection implements Serializable {
    private static final long serialVersionUID = -6923076683205732653L;
    private String region;
    private String[] ipRanges;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String[] getIpRanges() {
        return ipRanges;
    }

    public void setIpRanges(String[] ipRanges) {
        this.ipRanges = ipRanges;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("region", region)
                .append("ipRanges", ipRanges)
                .toString();
    }
}
