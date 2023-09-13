package com.wre.game.api.config;

import com.alibaba.fastjson.JSON;
import com.wre.game.api.component.DataComponent;
import com.wre.game.api.data.GameInfos;
import com.wre.game.api.data.ProductList;
import com.wre.game.api.util.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

    private static final Logger logger = LoggerFactory.getLogger(GameConfig.class);
    private static GameInfos gameInfos = new GameInfos();
    private static ProductList productList = new ProductList();
    private static String CONFIG_FILE_PATH;
    @Value("${config.game}")
    private String configFilePath;
    @Value("${config.product-list}")
    private String productListFilePath;
    @Autowired
    private DataComponent dataComponent;

    public static GameInfos getGameInfos() {
        return gameInfos;
    }

    public GameInfos reloadGameInfosFromPath() {
        String gameConfig = GameUtils.getGameConfigString(configFilePath);
        dataComponent.setGameConfigJson(gameConfig);
        gameInfos = GameUtils.getGameInfosFromGameConfig(gameConfig);
        logger.info("reloadGameInfosFromPath: {} ", String.valueOf(gameInfos));
        return gameInfos;
    }

    public GameInfos reloadGameInfosFromRedis() {
        String gameConfig = dataComponent.getGameConfigJson();
        gameInfos = GameUtils.getGameInfosFromGameConfig(gameConfig);
        logger.info("reloadGameInfosFromRedis: {} ", String.valueOf(gameInfos));
        return gameInfos;
    }

    public ProductList reloadProductListFromPath() {
        String json = GameUtils.getProductListString(productListFilePath);
        dataComponent.setProductListJson(json);
        productList = GameUtils.getProductListFromJson(json);
        logger.info("reloadProductListFromPath: {} ", String.valueOf(productList));
        return productList;
    }

    public ProductList reloadProductListFromRedis() {
        String json = dataComponent.getProductListJson();
        productList = GameUtils.getProductListFromJson(json);
        logger.info("reloadProductListFromRedis: {} ", String.valueOf(productList));
        return productList;
    }

    @Bean
    public GameInfos gameInfos() {
        //get game config from redis first.
        String gameConfig = dataComponent.getGameConfigJson();
        logger.info("查看加载:"+gameConfig);
        if (gameConfig != null) {
            logger.info("get game info from redis");
            gameInfos = GameUtils.getGameInfosFromGameConfig(gameConfig);
        }

        if (gameInfos == null || gameInfos.getGameInfoList() == null) {
            logger.info("get game info from game config file path");
            // get game config from files
            gameInfos = GameUtils.getGameInfosFromPath(configFilePath);
        }
        logger.info("启动加载GameInfo："+JSON.toJSONString(gameInfos));
        return gameInfos;
    }

    @Bean
    public ProductList productList() {
//get game config from redis first.
        String json = dataComponent.getProductListJson();
        if (json != null) {
            logger.info("get product list from redis");
            productList = GameUtils.getProductListFromJson(json);
        }

        logger.info("productList: {} ", String.valueOf(productList));

        if (productList == null || productList.getProductListMap() == null) {
            logger.info("get productList from file path");
            // get productList config from files
            productList = GameUtils.getProductListFromPath(productListFilePath);
        }

        return productList;
    }

}