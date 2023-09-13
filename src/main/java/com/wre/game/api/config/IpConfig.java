package com.wre.game.api.config;

import com.wre.game.api.component.DataComponent;
import com.wre.game.api.data.IpInfos;
import com.wre.game.api.util.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IpConfig {

    private static final Logger logger = LoggerFactory.getLogger(IpConfig.class);
    private static IpInfos ipInfos = new IpInfos();
    private static String CONFIG_FILE_PATH;
    @Value("${config.ip}")
    private String configFilePath;
    @Autowired
    private DataComponent dataComponent;

    public static IpInfos getIpInfos() {
        return ipInfos;
    }

    public IpInfos reloadIpInfosFromPath() {
        String ipConfig = GameUtils.getIpConfigString(configFilePath);
        dataComponent.setIpConfigJson(ipConfig);
        ipInfos = GameUtils.getIpInfosFromIpConfig(ipConfig);
        logger.info("reloadIpInfosFromPath: {} ", String.valueOf(ipInfos));
        return ipInfos;
    }

    public IpInfos reloadIpInfosFromRedis() {
        String ipConfig = dataComponent.getIpConfigJson();
        ipInfos = GameUtils.getIpInfosFromIpConfig(ipConfig);
        logger.info("reloadIpInfosFromRedis: {} ", String.valueOf(ipInfos));
        return ipInfos;
    }

    @Bean
    public IpInfos ipInfos() {
        //get game config from redis first.
        String ipConfig = dataComponent.getIpConfigJson();
        if (ipConfig != null) {
            logger.info("get ip info from redis");
            ipInfos = GameUtils.getIpInfosFromIpConfig(ipConfig);
        }

        logger.info("ipInfos: {} ", String.valueOf(ipInfos));

        if (ipInfos == null || ipInfos.getBlockIpList() == null) {
            logger.info("get ip info from ip config file path: {}", configFilePath);
            // get game config from files
            ipInfos = GameUtils.getIpInfosFromPath(configFilePath);
        }

        return ipInfos;
    }

}