package com.wre.game.api.adapter.impl;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.adapter.QqAdapter;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.GameInfo;
import com.wre.game.api.data.QqSession;
import com.wre.game.api.data.WechatSession;
import com.wre.game.api.exception.AuthException;
import com.wre.game.api.util.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class QqAdapterImpl implements QqAdapter {

    private static final Logger logger = LoggerFactory.getLogger(QqAdapterImpl.class);

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    OkHttpUtil okHttpUtil;
    /**
     * GET https://api.q.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=023Yibm92BnsOQ0iy4p92UPwm92Yibm8&grant_type=authorization_code
     *
     * @param code
     * @link https://q.qq.com/wiki/develop/miniprogram/server/open_port/port_login.html#code2session
     */
    @Override
    public QqSession codeToSession(String code, String appId) {
        logger.info("code:{}, appId:{}", code, appId);

        GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(appId);

        if (gameInfo == null) {
            throw new AuthException(RtCode.RT_NOT_FOUND_APP_ID);
        }

        //params
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", gameInfo.getAppSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");

//        String url = "https://api.q.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type={grant_type}";
//
//        String json = restTemplate.getForObject(url, String.class, map);
//
//        QqSession obj = JSONObject.parseObject(json, QqSession.class);

//        System.out.println("QQ request -> params: " + map.toString());
        String url = "https://test/sns/jscode2session";
        String json = okHttpUtil.get(url, map);
//        System.out.println("QQ response -> : " + json);

        QqSession obj = JSONObject.parseObject(json, QqSession.class);
//        System.out.println("QQ response parsed obj -> " + String.valueOf(obj));
//        System.out.println("");

        return obj;
    }
}