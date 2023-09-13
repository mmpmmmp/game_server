package com.wre.game.api.dao;

import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.data.dto.UserRankDTO;
import com.wre.game.api.data.entity.UserGameData;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface UserGameDataDao {

    int insertUserGameData(UserGameData userGameData);

    int updateUserGameData(UserGameData userGameData);

    UserGameData selectUserGameData(@Param("uuid") String uuid, @Param("appId") String appId);

    List<UserRankDTO> selectGameDataList(@Param("appId") String appId, @Param("offset") Integer offset, @Param("limit") Integer limit, @Param("orderBy") String orderBy);

    List<UserRankDTO> selectGameDataRank(@Param("appId") String appId, @Param("limit") Integer limit);

    @Transactional
    int clearGameData(@Param("uuid") String uuid);
}