package com.wre.game.api.service;

import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.dto.UserRankDTO;
import com.wre.game.api.data.entity.UserGameData;
import com.wre.game.api.data.entity.UserGameDataStorage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDataStorageService {
    
    RtCode saveUserGameDataStorage(UserGameDataStorage userGameDataStorage);

    UserGameDataStorage getUserGameDataStorage(String uuid, String appId);

    UserGameDataStorage getUserGameDataStorageByUserId(String userId, String appId);

    void removeData(String uuid, String appId);

}
