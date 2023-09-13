package com.wre.game.api.dao;

import com.wre.game.api.data.entity.UserActionGame;
import com.wre.game.api.data.entity.UserActionShare;
import org.springframework.stereotype.Component;

@Component
public interface UserActionGameDao {

    int insertUseActionGame(UserActionGame userActionGame);

}