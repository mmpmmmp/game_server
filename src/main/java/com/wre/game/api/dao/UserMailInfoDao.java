package com.wre.game.api.dao;

import com.wre.game.api.data.entity.UserMailInfo;
import com.wre.game.api.data.param.MailChangeParam;
import org.apache.ibatis.annotations.Param;

public interface UserMailInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMailInfo record);

    int insertSelective(UserMailInfo record);

    UserMailInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMailInfo record);

    int updateByPrimaryKey(UserMailInfo record);

    int changeState(MailChangeParam mailChangeParam);

    int deleteAwardedUserMailInfo();

    UserMailInfo selectIfExit(@Param("userId") String userId, @Param("mailId") Integer mailId);
}