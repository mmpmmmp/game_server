package com.wre.game.api.dao;

import com.wre.game.api.data.entity.UserLoginHistory;
import com.wre.game.api.data.entity.UserRetention;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface UserLoginHistoryDao {

    int insertUserLoginHistory(UserLoginHistory userLoginHistory);

    int backupUserLoginHistory(@Param("newTableName") String newTableName, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int deleteUserLoginHistoryBefore(@Param("loginTime") Date loginTime);

    List<UserRetention> selectNewUserRentention(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("appName") String appName,
                                                @Param("shareType") String shareType, @Param("shareFromImage") String shareFromImage);

    List<UserRetention> selectActiveUserRentention(@Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("appName") String appName,
                                                   @Param("shareType") String shareType, @Param("shareFromImage") String shareFromImage);

    Integer selectActiveCount( @Param("startTime") Date startTime, @Param("endTime") Date endTime, @Param("appName") String appName,
                               @Param("shareType") String shareType, @Param("shareFromImage") String shareFromImage);

}