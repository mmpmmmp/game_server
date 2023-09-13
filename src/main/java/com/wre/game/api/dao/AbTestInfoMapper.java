package com.wre.game.api.dao;

import com.wre.game.api.data.entity.AbTestInfo;
import com.wre.game.api.data.entity.User;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Component
public interface AbTestInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AbTestInfo record);

    int insertSelective(AbTestInfo record);

    AbTestInfo selectByPrimaryKey(Integer id);

    List<AbTestInfo> selectByTime(Date time);

    List<AbTestInfo> selectByAll();

    int updateByPrimaryKeySelective(AbTestInfo record);

    int updateByPrimaryKey(AbTestInfo record);

    AbTestInfo selectByAppVersion(User user);
}