package com.wre.game.api.dao;

import com.wre.game.api.data.entity.GameCodeUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface GameCodeUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GameCodeUser record);

    int insertSelective(GameCodeUser record);

    GameCodeUser selectByPrimaryKey(Integer id);

    Integer selectByUser(@Param("codeId")Integer codeId,@Param("userId")String userId);

    int updateByPrimaryKeySelective(GameCodeUser record);

    int updateByPrimaryKey(GameCodeUser record);
}