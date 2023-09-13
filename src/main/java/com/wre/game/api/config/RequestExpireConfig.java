package com.wre.game.api.config;

import com.alibaba.fastjson.JSON;
import com.wre.game.api.component.DataComponent;
import com.wre.game.api.data.GameInfos;
import com.wre.game.api.data.RequestExpireInfos;
import com.wre.game.api.util.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequestExpireConfig {
    private static final Logger logger = LoggerFactory.getLogger(IpConfig.class);
    private static RequestExpireInfos requestExpireInfos = new RequestExpireInfos();
    @Value("${config.request-expire}")
    private String requestExpirePath;
    @Autowired
    private DataComponent dataComponent;

    public static  RequestExpireInfos getRequestExpireInfos(){
        return requestExpireInfos;
    }

    public RequestExpireInfos reloadRequestExpireFromPath(){
        String requestExpireConfig = GameUtils.getRequestExpireConfigString(requestExpirePath);
        dataComponent.setRequestExpireConfigJson(requestExpireConfig);
        requestExpireInfos = GameUtils.getRequestExpireInfosFromConfig(requestExpireConfig);
        logger.info("reloadGameInfosFromPath: {} ", String.valueOf(requestExpireInfos));
        return requestExpireInfos;
    }

    public RequestExpireInfos reloadRequestExpireFromRedis(){
        String requestExpireConfig = dataComponent.getGameConfigJson();
        requestExpireInfos = GameUtils.getRequestExpireInfosFromConfig(requestExpireConfig);
        logger.info("reloadGameInfosFromRedis: {} ", String.valueOf(requestExpireInfos));
        return requestExpireInfos;
    }

    @Bean
    public RequestExpireInfos requestExpireInfos(){
        //get game config from redis first.
        String requestExpireConfig = dataComponent.getRequestExpireJson();
        logger.info("查看加载:"+requestExpireConfig);
        if (requestExpireConfig != null) {
            logger.info("get RequestExpireInfos from redis");
            requestExpireInfos = GameUtils.getRequestExpireInfosFromConfig(requestExpireConfig);
        }

        if (requestExpireInfos == null || requestExpireInfos.getRequestExpireInfoList() == null) {
            logger.info("get RequestExpireInfos from game config file path");
            // get game config from files
            requestExpireInfos = GameUtils.getRequestExpireFromPath(requestExpirePath);
        }
        logger.info("启动加载RequestExpireInfos："+ JSON.toJSONString(requestExpireInfos));
        return requestExpireInfos;
    }
}
