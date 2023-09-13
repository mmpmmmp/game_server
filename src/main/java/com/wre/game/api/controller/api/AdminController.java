package com.wre.game.api.controller.api;

import com.wre.game.api.config.GameConfig;
import com.wre.game.api.config.IpConfig;
import com.wre.game.api.config.ProductListConfig;
import com.wre.game.api.config.RequestExpireConfig;
import com.wre.game.api.constants.ApiResource;
import com.wre.game.api.data.GameInfos;
import com.wre.game.api.data.IpInfos;
import com.wre.game.api.data.ProductList;
import com.wre.game.api.data.RequestExpireInfos;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * AdminController
 */
@RestController
@RequestMapping(value = ApiResource.API_ADMIN_CONFIG)
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    GameConfig gameConfig;

    @Autowired
    IpConfig ipConfig;

    @Autowired
    RequestExpireConfig requestExpireConfig;

    /**
     * 刷新GameConfig
     */
    @RequestMapping(value = "/reloadGameConfig", method = {RequestMethod.GET})
    public GameInfos reloadGameInfo(@RequestParam(required = false) String from) {
        GameInfos gi = null;
        if(StringUtils.equals(from,"redis")){
            gi = gameConfig.reloadGameInfosFromRedis();
        }else {
            gi = gameConfig.reloadGameInfosFromPath();
        }

        logger.info("ReloadGameConfig: {}", gi);
        logger.info("Current GameConfig: {}", GameConfig.getGameInfos());
        return gi;
    }


    /**
     * 刷新ProductList
     */
    @RequestMapping(value = "/reloadProductListConfig", method = {RequestMethod.GET})
    public ProductList reloadProductList(@RequestParam(required = false) String from) {
        ProductList gi = null;
        if(StringUtils.equals(from,"redis")){
            gi = gameConfig.reloadProductListFromRedis();
        }else {
            gi = gameConfig.reloadProductListFromPath();
        }

        logger.info("reloadProductList: {}", gi);
        logger.info("Current ProductList: {}", ProductListConfig.getProductList());
        return gi;
    }

    /**
     * 刷新IpConfig
     */
    @RequestMapping(value = "/reloadIpConfig", method = {RequestMethod.GET})
    public IpInfos reloadIpInfo(@RequestParam(required = false) String from) {
        IpInfos ipInfos = null;
        if(StringUtils.equals(from,"redis")){
            ipInfos = ipConfig.reloadIpInfosFromRedis();
        }else {
            ipInfos = ipConfig.reloadIpInfosFromPath();
        }

        logger.info("ReloadIpConfig: {}", ipInfos);
        logger.info("Current IpConfig: {}", IpConfig.getIpInfos());
        return ipInfos;
    }

    /**
     * 刷新RequestExpireConfig
     */
    @RequestMapping(value = "/reloadRequestExpireConfig", method = {RequestMethod.GET})
    public RequestExpireInfos reloadRequestExpire(@RequestParam(required = false) String from) {
        RequestExpireInfos re = null;
        if(StringUtils.equals(from,"redis")){
            re = requestExpireConfig.reloadRequestExpireFromRedis();
        }else {
            re = requestExpireConfig.reloadRequestExpireFromPath();
        }
        logger.info("reload RequestExpireConfig: {}", re);
        logger.info("Current RequestExpireConfig: {}", RequestExpireConfig.getRequestExpireInfos());
        return re;
    }
}
