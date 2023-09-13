package com.wre.game.api.channel.login;

import com.wre.game.api.channel.ChannelLogin;
import com.wre.game.api.data.IpBlock;
import com.wre.game.api.data.dto.UserLoginDTO;
import com.wre.game.api.data.entity.LogingRepUser;
import com.wre.game.api.data.param.UserLoginReqParam;
import com.wre.game.api.message.ChannelInfo;
import org.springframework.stereotype.Service;

@Service(ChannelInfo.ChannelCode.QQ_MINI)
public class QqMiniLogin implements ChannelLogin {

    @Override
    public UserLoginDTO login(LogingRepUser logingRepUser, UserLoginReqParam userLoginReqParam, IpBlock ipBlock) {
        return null;
    }
}
