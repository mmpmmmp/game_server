package com.wre.game.api.data.entity;

import java.util.Date;

public class UserRetention {

    private String date;
    private Long retentionCount;
    private Double retentionRate;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getRetentionCount() {
        return retentionCount;
    }

    public void setRetentionCount(Long retentionCount) {
        this.retentionCount = retentionCount;
    }

    public Double getRetentionRate() {
        return retentionRate;
    }

    public void setRetentionRate(Double retentionRate) {
        this.retentionRate = retentionRate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserRetention{");
        sb.append("date=").append(date);
        sb.append(", retentionCount=").append(retentionCount);
        sb.append(", retentionRate=").append(retentionRate);
        sb.append('}');
        return sb.toString();
    }
}
