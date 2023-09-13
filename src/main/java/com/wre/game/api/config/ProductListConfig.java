package com.wre.game.api.config;

import com.wre.game.api.component.DataComponent;
import com.wre.game.api.data.ProductList;
import com.wre.game.api.data.Product;
import com.wre.game.api.data.ProductList;
import com.wre.game.api.util.GameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductListConfig {

    private static final Logger logger = LoggerFactory.getLogger(ProductListConfig.class);
    private static ProductList productList = new ProductList();
    private static String PRODUCT_FILE_PATH;
    @Value("${config.product-list}")
    private String configFilePath;
    @Autowired
    private DataComponent dataComponent;

    public static ProductList getProductList() {
        return productList;
    }

    public ProductList reloadProductListFromPath() {
        String productListJson = GameUtils.getProductListString(configFilePath);
        dataComponent.setProductListJson(productListJson);
        productList = GameUtils.getProductListFromJson(productListJson);
        logger.info("reloadProductListFromPath: {} ", String.valueOf(productList));
        return productList;
    }

    public ProductList reloadProductListFromRedis() {
        String productListJson = dataComponent.getProductListJson();
        productList = GameUtils.getProductListFromJson(productListJson);
        logger.info("reloadProductListFromRedis: {} ", String.valueOf(productList));
        return productList;
    }

    @Bean
    public ProductList productList() {
        //get game config from redis first.
        String productListJson = dataComponent.getProductListJson();
        if (productListJson != null) {
            logger.info("get product list info from redis");
            productList = GameUtils.getProductListFromJson(productListJson);
        }

        logger.info("productList: {} ", String.valueOf(productList));

        if (productList == null || productList.getProductListMap() == null) {
            logger.info("get product list from file path");
            // get game config from files
            productList = GameUtils.getProductListFromPath(configFilePath);
        }

        return productList;
    }

}