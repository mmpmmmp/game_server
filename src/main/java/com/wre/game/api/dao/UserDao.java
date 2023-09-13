package com.wre.game.api.dao;

import com.wre.game.api.data.dto.UserShareFromDTO;
import com.wre.game.api.data.entity.User;
import com.wre.game.api.data.entity.UserShareFrom;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface UserDao {

    int insertUser(User user);

    int insertUserShareFrom(@Param("fromUuid") String fromUuid, @Param("toUuid") String toUuid, @Param("shareType") String shareType, @Param("appId") String appId, @Param("appName") String appName);

    int insertUserShareFromFunctional(@Param("fromUuid") String fromUuid, @Param("toUuid") String toUuid, @Param("shareType") String shareType, @Param("appId") String appId, @Param("appName") String appName);

    int deleteUserById(Long id);

    int updateUserByGroupType(User user);

    int updateUserById(User user);

    int updateLastLoginTimeById(Long userId);

    User selectUserById(Long id);

    User selectUserByOpenId(String openId);

    User selectUserByOpenIdAndAppId(@Param("openId")String openId, @Param("appId")String appId);

    /**
     * 根据渠道id和游戏appId返回该玩家信息
     * @param channelId  渠道id
     * @param appId 游戏appId
     * 2020年8月21日15:23:22 申欣武
     * @return
     */
    User selectUserByChannelIdAndAppId(@Param("channelId")String channelId, @Param("appId")String appId,@Param("channelName")String channelName);


    List<UserShareFromDTO> selectShareStatistics(@Param("fromUuid") String fromUuid, @Param("shareType") String shareType, @Param("appId") String appId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Integer selectExistShareFrom(@Param("fromUuid") String fromUuid, @Param("toUuid") String toUuid, @Param("shareType") String shareType, @Param("appId") String appId);

    List<UserShareFromDTO> selectShareStatisticsFunctional(@Param("fromUuid") String fromUuid, @Param("shareType") String shareType, @Param("appId") String appId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    Integer selectExistShareFromFunctional(@Param("fromUuid") String fromUuid, @Param("toUuid") String toUuid, @Param("shareType") String shareType, @Param("appId") String appId);

    UserShareFrom selectShareFromFunctional(@Param("fromUuid") String fromUuid, @Param("toUuid") String toUuid, @Param("shareType") String shareType, @Param("appId") String appId);

    List<User> selectUserList();

    Integer selectCountCreatedAt(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("appName") String appName,
                                 @Param("shareType") String shareType, @Param("shareFromImage") String shareFromImage);

    int getInvitedFriends(@Param("fromUuid") String fromUuid, @Param("shareType") String shareType, @Param("appId") String appId);

    User selectUserByUuid(String uuid);
}