package com.wre.game.api.dao;

import com.wre.game.api.data.entity.GameCode;
import com.wre.game.api.data.entity.GameCodeWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface GameCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameCodeWithBLOBs record);

    int insertSelective(GameCodeWithBLOBs record);


    GameCodeWithBLOBs selectByCode(@Param("code") String code,@Param("appId") String appId);

    GameCodeWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GameCodeWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GameCodeWithBLOBs record);

    int updateByPrimaryKey(GameCode record);
}