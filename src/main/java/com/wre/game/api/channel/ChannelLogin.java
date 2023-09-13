package com.wre.game.api.channel;

import com.wre.game.api.data.IpBlock;
import com.wre.game.api.data.dto.UserLoginDTO;
import com.wre.game.api.data.entity.LogingRepUser;
import com.wre.game.api.data.param.UserLoginReqParam;

public interface ChannelLogin {

    public UserLoginDTO login(LogingRepUser logingRepUser, UserLoginReqParam userLoginReqParam, IpBlock ipBlock);

}
