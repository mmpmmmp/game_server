package com.wre.game.api.service;

import com.wre.game.api.constants.ApiResponseDataMessage;

public interface GameCodeService {


    /**
     * 根据code值返回相应的奖励
     * @param code
     * @return
     */
    ApiResponseDataMessage getAward(String userId,String code,String appId);


    /**
     * 消耗兑换码
     * @param code
     * @return
     */
    ApiResponseDataMessage useCode(String userId,String code,String appId);

}
