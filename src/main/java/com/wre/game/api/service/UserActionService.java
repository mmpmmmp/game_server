package com.wre.game.api.service;

import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.entity.UserActionAd;
import com.wre.game.api.data.entity.UserActionGame;
import com.wre.game.api.data.entity.UserActionShare;

public interface UserActionService {
    
    public RtCode saveUserActionShare(UserActionShare action);

    public RtCode saveUserActionAd(UserActionAd action);

    public RtCode saveUserActionGame(UserActionGame action);

}
