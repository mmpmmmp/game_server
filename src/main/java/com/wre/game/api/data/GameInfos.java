package com.wre.game.api.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameInfos implements Serializable {

    private static final long serialVersionUID = -8524499322396356856L;
    private Map<String, GameInfo> gameInfoMap = new HashMap<>();

    public Map<String, GameInfo> getGameInfoMap() {
        return gameInfoMap;
    }

    public void setGameInfoMap(Map<String, GameInfo> gameInfoMap) {
        this.gameInfoMap = gameInfoMap;
    }

    private List<GameInfo> gameInfoList;

    public GameInfo getByAppId(String appId) {
        return gameInfoMap.get(appId);
    }

    public List<GameInfo> getGameInfoList() {
        return gameInfoList;
    }

    public void setGameInfoList(List<GameInfo> gameInfoList) {

        if (gameInfoList != null && gameInfoList.size() > 0) {
            for (GameInfo gameInfo : gameInfoList) {
                gameInfoMap.put(gameInfo.getAppId(), gameInfo);
            }
        }

        this.gameInfoList = gameInfoList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GameInfos{");
        sb.append("gameInfoMap=").append(gameInfoMap);
        sb.append(", gameInfoList=").append(gameInfoList);
        sb.append('}');
        return sb.toString();
    }
}
