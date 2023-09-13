package com.wre.game.api.component;

public interface DataComponent {

    public void setGameData(String uuid, String appId, String data);
    public String getGameData(String uuid, String appId);
    public void delGameDataKey(String uuid, String appId);

    public void setGameConfigJson(String data);
    public String getGameConfigJson();

    public void setProductListJson(String data);
    public String getProductListJson();

    public void setIpConfigJson(String data);
    public String getIpConfigJson();

    public void setGameRankData(String appId, String data, int seconds);
    public String getGameRankData(String appId);


    public void setAppAccessToken(String appId, String data, int seconds);
    public String getAppAccessToken(String appId);

    void setRequestExpireConfigJson(String requestExpireConfig);

    String getRequestExpireJson();

}
