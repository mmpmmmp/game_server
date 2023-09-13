package com.wre.game.api.dao;

import com.wre.game.api.data.entity.UserGem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserGemMapper {

    public int updateUserGem(UserGem userGem);

    public UserGem getUserGem(@Param("userId") Long userId,@Param("appId") String appId);

    public int insertUserGem(UserGem userGem);
}
