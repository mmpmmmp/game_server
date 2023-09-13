package com.wre.game.api.dao;

import com.wre.game.api.data.entity.UserChannel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserChannelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserChannel record);

    int insertSelective(UserChannel record);

    UserChannel selectByPrimaryKey(Integer id);

    /**
     * 根据uuid去找到这条信息
     * @param uuid
     * @return
     */
    UserChannel selectByUUID(@Param("uuid") String uuid,@Param("channelName")String channelName);

    /**
     * openId
     * @param openId
     * @return
     */
    UserChannel selectByOpenId(@Param("openId") String openId,@Param("channelName")String channelName);
    /**
     * 根据渠道id和appId获取渠道信息
     * @param channelId
     * @param appId
     * @return
     */
    UserChannel selectChannelIdAndAppId(@Param("channelId")String channelId, @Param("appId")String appId);

    int updateByPrimaryKeySelective(UserChannel record);

    int updateByPrimaryKey(UserChannel record);
}