package com.wre.game.api.data.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 *
 */
public class UserRanksDTO {


    private List<UserRankDTO> rankList;

    public List<UserRankDTO> getRankList() {
        return rankList;
    }

    public void setRankList(List<UserRankDTO> rankList) {
        this.rankList = rankList;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("rankList", rankList)
                .toString();
    }
}
