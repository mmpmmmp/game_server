package com.wre.game.api.entity;

/***/
public class RechargeUtil {
    private static Integer port;

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        RechargeUtil.port = port;
    }

    public static void initStaticProperties(RechargeInfo rechargeInfo){
        port = rechargeInfo.getPort();
    }

    public static Integer getStaticProperties() {
        return port;
    }
}
