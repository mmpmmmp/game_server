package com.wre.game.api.service;

import com.wre.game.api.data.entity.UserRetention;

import java.util.Date;
import java.util.List;

public interface UserLoginHistotryService {

    public List<UserRetention> getNewUserRentention(Date date, String appName, String shareType, String shareFromImage);

    public List<UserRetention> getActiveUserRentention(Date date, String appName, String shareType, String shareFromImage);

}
