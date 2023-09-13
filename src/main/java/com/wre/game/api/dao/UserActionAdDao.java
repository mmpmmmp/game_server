package com.wre.game.api.dao;

import com.wre.game.api.data.entity.UserActionAd;
import com.wre.game.api.data.entity.UserActionShare;
import org.springframework.stereotype.Component;

@Component
public interface UserActionAdDao {

    int insertUseActionAd(UserActionAd userActionAd);

}