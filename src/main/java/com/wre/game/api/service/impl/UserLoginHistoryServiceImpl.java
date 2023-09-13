package com.wre.game.api.service.impl;

import com.wre.game.api.dao.UserDao;
import com.wre.game.api.dao.UserLoginHistoryDao;
import com.wre.game.api.data.entity.UserRetention;
import com.wre.game.api.service.UserLoginHistotryService;
import com.wre.game.api.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component

public class UserLoginHistoryServiceImpl implements UserLoginHistotryService {
    private static final Logger logger = LoggerFactory.getLogger(UserActionServiceImpl.class);

    @Autowired
    private UserLoginHistoryDao userLoginHistoryDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<UserRetention> getNewUserRentention(Date date, String appName, String shareType, String shareFromImage) {
        Date startTime = DateUtil.getDayBegin(date);
        Date endTime = DateUtil.getDayEnd(date);
        List<UserRetention> list = userLoginHistoryDao.selectNewUserRentention(startTime, endTime, appName, shareType, shareFromImage);

        Integer createdCount = userDao.selectCountCreatedAt(startTime, endTime, appName, shareType, shareFromImage);

        if (list != null && list.size() > 0 && createdCount != 0 && createdCount != null) {
            Double totalCount = Double.valueOf(createdCount);

            for (int i = 0; i < list.size(); i++) {
                UserRetention ur = list.get(i);
                Double rate = null;
                try {
                    rate = ur.getRetentionCount() / totalCount;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ur.setRetentionRate(rate);
            }
        }
        return list;
    }

    @Override
    public List<UserRetention> getActiveUserRentention(Date date, String appName, String shareType, String shareFromImage) {
        Date startTime = DateUtil.getDayBegin(date);
        Date endTime = DateUtil.getDayEnd(date);
        List<UserRetention> list = userLoginHistoryDao.selectActiveUserRentention(startTime, endTime, appName, shareType, shareFromImage);

        Integer createdCount = userLoginHistoryDao.selectActiveCount(startTime, endTime, appName, shareType,shareFromImage);

        if (list != null && list.size() > 0 && createdCount != 0 && createdCount != null) {
            Double totalCount = Double.valueOf(createdCount);

            for (int i = 0; i < list.size(); i++) {
                UserRetention ur = list.get(i);

                if (i == 0) {

                }
                Double rate = null;
                try {
                    rate = ur.getRetentionCount() / totalCount;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ur.setRetentionRate(rate);
            }
        }
        return list;
    }
}
