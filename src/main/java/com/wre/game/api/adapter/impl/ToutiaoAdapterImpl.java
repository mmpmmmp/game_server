package com.wre.game.api.adapter.impl;

import com.alibaba.fastjson.JSONObject;
import com.wre.game.api.adapter.ToutiaoAdapter;
import com.wre.game.api.config.GameConfig;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.data.GameInfo;
import com.wre.game.api.data.ToutiaoSession;
import com.wre.game.api.exception.AuthException;
import com.wre.game.api.util.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Component
public class ToutiaoAdapterImpl implements ToutiaoAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ToutiaoAdapterImpl.class);

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    OkHttpUtil okHttpUtil;

    /**
     * https://developer.toutiao.com/api/apps/jscode2session?appid=APPID&secret=SECRET&code=CODE&anonymous_code=anonymous_code
     *
     * @param code
     * @param anonymousCode code 和 anonymous_code 至少要有一个
     * @link http://developer.toutiao.com/docs/open/jscode2session.html
     */
    @Override
    public ToutiaoSession codeToSession(String code, String anonymousCode, String appId) {
        logger.info("code:{}, anonymousCode{}, appId:{}", code, anonymousCode, appId);

        GameInfo gameInfo = GameConfig.getGameInfos().getByAppId(appId);

        if (gameInfo == null) {
            throw new AuthException(RtCode.RT_NOT_FOUND_APP_ID);
        }

        //params
        Map<String, String> map = new HashMap<>();
        map.put("appid", appId);
        map.put("secret", gameInfo.getAppSecret());
        map.put("code", code);
        map.put("anonymous_code", anonymousCode);
        logger.info(map.toString());
//        System.out.println("Toutiao request -> params: " + map.toString());

//        String url = "https://test/api/apps/jscode2session?appid={appid}&secret={secret}&code={code}&anonymous_code={anonymous_code}";
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, map);
//        System.out.println("Toutiao response -> : " + responseEntity.toString());
//
//        String json = responseEntity.getBody();

        String url = "https://test/api/apps/jscode2session";
        String json = okHttpUtil.get(url, map);
//        System.out.println("Toutiao response -> : " + json);

        ToutiaoSession obj = JSONObject.parseObject(json, ToutiaoSession.class);
//        System.out.println("Toutiao response parsed obj -> " + String.valueOf(obj));
//        System.out.println("");

        return obj;
    }
}
