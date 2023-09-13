package com.wre.game.api.dao;

import com.wre.game.api.data.entity.UserSubscribe;
import com.wre.game.api.data.entity.UserSubscribeKey;

public interface UserSubscribeDao {
    int deleteByPrimaryKey(UserSubscribeKey key);

    int insert(UserSubscribe record);

    int insertSelective(UserSubscribe record);

    UserSubscribe selectByPrimaryKey(UserSubscribeKey key);

    int updateByPrimaryKeySelective(UserSubscribe record);

    int updateByPrimaryKey(UserSubscribe record);


}