package com.wre.game.api.service;

import com.wre.game.api.data.entity.AbTestInfo;
import com.wre.game.api.message.MessageResp;

import java.util.List;

public interface AbTestInfoService {


    public MessageResp getABList();

    public MessageResp delAB(List<Integer> ids);

    public MessageResp addAB(AbTestInfo abTestInfo);


}
