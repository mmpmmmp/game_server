package com.wre.game.api.dao;

import com.wre.game.api.data.dto.UserRankDTO;
import com.wre.game.api.data.entity.UserGameData;
import com.wre.game.api.data.entity.UserGameDataStorage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserGameDataStorageDao {

    int insertOrUpdateUserGameDataStorage(UserGameDataStorage userGameDataStorage);

    UserGameDataStorage selectUserGameDataStorage(@Param("uuid") String uuid, @Param("appId") String appId);

    UserGameDataStorage selectUserGameDataStorageByUserId(@Param("userId") String userId, @Param("appId") String appId);

    void removeData(@Param("uuid") String uuid, @Param("appId") String appId);
}