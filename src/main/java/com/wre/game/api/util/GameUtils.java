package com.wre.game.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wre.game.api.data.GameInfos;
import com.wre.game.api.data.IpInfos;
import com.wre.game.api.data.ProductList;
import com.wre.game.api.data.RequestExpireInfos;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GameUtils {

    private static final Logger logger = LoggerFactory.getLogger(GameUtils.class);

    /**
     * 版本号比较
     *
     * @param v1
     * @param v2
     * @return 0代表相等，1代表左边大，-1代表右边大
     * Utils.compareVersion("1.0.358_20180820090554","1.0.358_20180820090553")=1
     */
    public static int compareVersion(String v1, String v2) {
        if (v1.equals(v2)) {
            return 0;
        }
        String[] version1Array = v1.split("[._]");
        String[] version2Array = v2.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(version1Array[index])
                - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    public static String getGameConfigString(String configFilePath) {
        try {
            File file = ResourceUtils.getFile(configFilePath);
            FileInputStream fis = new FileInputStream(file);
            String gameConfig = IOUtils.toString(fis);
            IOUtils.closeQuietly(fis);
            return gameConfig;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read game info: {}, {} ", configFilePath, e.getMessage());
        }
        return null;
    }

    public static GameInfos getGameInfosFromGameConfig(String gameConfigJson) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<GameInfos> typeReference = new TypeReference<GameInfos>() {
        };
        try {
            GameInfos gameInfos = mapper.readValue(gameConfigJson, typeReference);
            return gameInfos;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read game info: {}, {} ", gameConfigJson, e.getMessage());
        }
        return null;
    }

    public static GameInfos getGameInfosFromPath(String configFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<GameInfos> typeReference = new TypeReference<GameInfos>() {
        };
        try {
            File file = ResourceUtils.getFile(configFilePath);
            GameInfos gameInfos = mapper.readValue(file, typeReference);
            logger.info("GameInfos:{}", gameInfos);
            return gameInfos;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read game info: {}, {} ", configFilePath, e.getMessage());
        }
        return null;
    }

    public static String getIpConfigString(String configFilePath) {
        try {
            File file = ResourceUtils.getFile(configFilePath);
            FileInputStream fis = new FileInputStream(file);
            String ipConfig = IOUtils.toString(fis);
            IOUtils.closeQuietly(fis);
            return ipConfig;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read ip info: {}, {} ", configFilePath, e.getMessage());
        }
        return null;
    }

    public static IpInfos getIpInfosFromIpConfig(String ipConfigJson) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<IpInfos> typeReference = new TypeReference<IpInfos>() {
        };
        try {
            IpInfos ipInfos = mapper.readValue(ipConfigJson, typeReference);
            return ipInfos;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read ip info: {}, {} ", ipConfigJson, e.getMessage());
        }
        return null;
    }

    public static IpInfos getIpInfosFromPath(String configFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<IpInfos> typeReference = new TypeReference<IpInfos>() {
        };
        try {
            File file = ResourceUtils.getFile(configFilePath);
            IpInfos ipInfos = mapper.readValue(file, typeReference);
            logger.info("IpInfos:{}", ipInfos);
            return ipInfos;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read game info: {}, {} ", configFilePath, e.getMessage());
        }
        return null;
    }


    public static String getProductListString(String configFilePath) {
        try {
            File file = ResourceUtils.getFile(configFilePath);
            FileInputStream fis = new FileInputStream(file);
            String productListStr = IOUtils.toString(fis);
            IOUtils.closeQuietly(fis);
            return productListStr;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read product list: {}, {} ", configFilePath, e.getMessage());
        }
        return null;
    }

    public static ProductList getProductListFromJson(String productListJson) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ProductList> typeReference = new TypeReference<ProductList>() {
        };
        try {
            ProductList productList = mapper.readValue(productListJson, typeReference);
            return productList;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read product list: {}, {} ", productListJson, e.getMessage());
        }
        return null;
    }

    public static ProductList getProductListFromPath(String configFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ProductList> typeReference = new TypeReference<ProductList>() {
        };
        try {
            File file = ResourceUtils.getFile(configFilePath);
            ProductList productList = mapper.readValue(file, typeReference);
            logger.info("ProductList:{}", productList);
            return productList;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read product list: {}, {} ", configFilePath, e.getMessage());
        }
        return null;
    }

    public static RequestExpireInfos getRequestExpireInfosFromConfig(String requestExpireConfig) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<RequestExpireInfos> typeReference = new TypeReference<RequestExpireInfos>() {
        };
        try {
            RequestExpireInfos requestExpireInfos = mapper.readValue(requestExpireConfig, typeReference);
            return requestExpireInfos;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read request expire info: {}, {} ", requestExpireConfig, e.getMessage());
        }
        return null;
    }

    public static String getRequestExpireConfigString(String requestExpirePath) {
        try {
            File file = ResourceUtils.getFile(requestExpirePath);
            FileInputStream fis = new FileInputStream(file);
            String requestExpireConfig = IOUtils.toString(fis);
            IOUtils.closeQuietly(fis);
            return requestExpireConfig;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read game info: {}, {} ", requestExpirePath, e.getMessage());
        }
        return null;
    }

    public static RequestExpireInfos getRequestExpireFromPath(String requestExpirePath) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<RequestExpireInfos> typeReference = new TypeReference<RequestExpireInfos>() {
        };
        try {
            File file = ResourceUtils.getFile(requestExpirePath);
            RequestExpireInfos requestExpireInfos = mapper.readValue(file, typeReference);
            logger.info("requestExpireInfos:{}", requestExpireInfos);
            return requestExpireInfos;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to read game info: {}, {} ", requestExpirePath, e.getMessage());
        }
        return null;
    }
}
