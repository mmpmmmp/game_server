package com.wre.game.api.adapter;

import com.wre.game.api.data.QqSession;

public interface QqAdapter {
    /**
     * https://q.qq.com/wiki/develop/miniprogram/server/open_port/port_login.html#code2session
     */

    QqSession codeToSession(String code, String appId);

}
