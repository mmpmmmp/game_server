package com.wre.game.api.adapter;

import com.wre.game.api.data.ToutiaoSession;

public interface ToutiaoAdapter {
    /**
     * https://developer.toutiao.com/api/apps/jscode2session?appid=APPID&secret=SECRET&code=CODE&anonymous_code=anonymous_code
     *
     * @param code
     * @param anonymousCode code 和 anonymous_code 至少要有一个
     * @link http://developer.toutiao.com/docs/open/jscode2session.html
     */

    ToutiaoSession codeToSession(String code, String anonymousCode, String appId);
}
