package com.wre.game.api.dao;

import com.wre.game.api.data.dto.UserMailDTO;
import com.wre.game.api.data.entity.MailConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MailConfigDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MailConfig record);

    int insertSelective(MailConfig record);

    MailConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MailConfig record);

    int updateByPrimaryKey(MailConfig record);

    List<UserMailDTO> getUserMail(@Param("userId") String userId,@Param("appId") String appId, @Param("timeStamp") Long timeStamp);
}