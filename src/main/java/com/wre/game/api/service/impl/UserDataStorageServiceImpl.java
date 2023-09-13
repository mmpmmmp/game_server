package com.wre.game.api.service.impl;

import com.wre.game.api.constants.RtCode;
import com.wre.game.api.dao.UserGameDataStorageDao;
import com.wre.game.api.data.entity.UserGameDataStorage;
import com.wre.game.api.service.UserDataStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDataStorageServiceImpl implements UserDataStorageService {
    private static final Logger logger = LoggerFactory.getLogger(UserDataStorageServiceImpl.class);
    @Autowired
    private UserGameDataStorageDao userGameDataStorageDao;

    @Override
    public RtCode saveUserGameDataStorage(UserGameDataStorage userGameDataStorage) {
        userGameDataStorageDao.insertOrUpdateUserGameDataStorage(userGameDataStorage);
        return RtCode.RT_SUCCESS;
    }


    @Override
    public UserGameDataStorage getUserGameDataStorage(String uuid, String appId) {
        return userGameDataStorageDao.selectUserGameDataStorage(uuid, appId);
    }

    @Override
    public UserGameDataStorage getUserGameDataStorageByUserId(String userId, String appId) {
        return userGameDataStorageDao.selectUserGameDataStorageByUserId(userId,appId);
    }

    @Override
    public void removeData(String uuid, String appId) {
        userGameDataStorageDao.removeData(uuid,appId);
    }
}
