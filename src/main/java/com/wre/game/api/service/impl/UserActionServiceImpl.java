package com.wre.game.api.service.impl;

import com.wre.game.api.component.SessionComponent;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.dao.UserActionAdDao;
import com.wre.game.api.dao.UserActionGameDao;
import com.wre.game.api.dao.UserActionShareDao;
import com.wre.game.api.data.entity.UserActionAd;
import com.wre.game.api.data.entity.UserActionGame;
import com.wre.game.api.data.entity.UserActionShare;
import com.wre.game.api.exception.ApiException;
import com.wre.game.api.service.UserActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserActionServiceImpl implements UserActionService {
    private static final Logger logger = LoggerFactory.getLogger(UserActionServiceImpl.class);
    @Autowired
    private UserActionAdDao userActionAdDao;
    @Autowired
    private UserActionGameDao userActionGameDao;
    @Autowired
    private UserActionShareDao userActionShareDao;
    @Autowired
    private SessionComponent sessionComponent;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public RtCode saveUserActionShare(UserActionShare action) {
        int i = userActionShareDao.insertUseActionShare(action);
        if (i > 0) {
            return RtCode.RT_SUCCESS;
        } else {
            throw new ApiException(RtCode.RT_INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public RtCode saveUserActionAd(UserActionAd action) {
        int i = userActionAdDao.insertUseActionAd(action);
        if (i > 0) {
            return RtCode.RT_SUCCESS;
        } else {
            throw new ApiException(RtCode.RT_INTERNAL_ERROR);
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public RtCode saveUserActionGame(UserActionGame action) {
        int i = userActionGameDao.insertUseActionGame(action);
        if (i > 0) {
            return RtCode.RT_SUCCESS;
        } else {
            throw new ApiException(RtCode.RT_INTERNAL_ERROR);
        }
    }
}
