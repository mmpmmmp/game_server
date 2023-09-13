package com.wre.game.api.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestExpireInfos implements Serializable {
    private static final long serialVersionUID = -5805142278903180914L;
    private List<RequestExpireInfo> requestExpireInfoList;
    private Map<String, RequestExpireInfo> requestExpireInfoMap = new HashMap<>();
    public List<RequestExpireInfo> getRequestExpireInfoList() {
        return requestExpireInfoList;
    }

    public void setRequestExpireInfoList(List<RequestExpireInfo> requestExpireInfoList) {
        if (requestExpireInfoList != null && requestExpireInfoList.size() > 0) {
            for (RequestExpireInfo requestExpireInfo : requestExpireInfoList) {
                requestExpireInfoMap.put(requestExpireInfo.getPath(),requestExpireInfo);
            }
        }
        this.requestExpireInfoList = requestExpireInfoList;
    }

    public Map<String, RequestExpireInfo> getRequestExpireInfoMap() {
        return requestExpireInfoMap;
    }

    public void setRequestExpireInfoMap(Map<String, RequestExpireInfo> requestExpireInfoMap) {
        this.requestExpireInfoMap = requestExpireInfoMap;
    }

    public RequestExpireInfo getByPath(String path){
        return requestExpireInfoMap.get(path);
    }


    @Override
    public String toString() {
        return "RequestExpireInfos{" +
                "requestExpireInfoList=" + requestExpireInfoList +
                '}';
    }
}
