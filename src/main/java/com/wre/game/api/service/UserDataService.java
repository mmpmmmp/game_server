package com.wre.game.api.service;

import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.dto.UserRankDTO;
import com.wre.game.api.data.entity.UserGameData;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserDataService {
    
    RtCode saveUserGameData(UserGameData userGameData);

    List<UserRankDTO> selectGameDataRank(@Param("appId") String appId, @Param("limit") Integer limit);

    ResponseEntity<ApiResponseMessage> clearGameData(String uuid);
}
