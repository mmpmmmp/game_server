package com.wre.game.api.constants;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;

public class ApiResponseDataMessage extends  ApiResponseMessage {

    private Object data;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponseDataMessage{" +
                "data='" + data + '\'' +
                "} " + super.toString();
    }

    public ApiResponseDataMessage() {
    }

    public ApiResponseDataMessage(RtCode rtCode, Object o) {
        this.setStatus(String.valueOf(rtCode.getHttpStatus().value()));
        this.setMessage(rtCode.getErrorMessage());
        this.setErrorCode(rtCode.getErrorCode());
        this.setErrorMessage(rtCode.getErrorMessage());
        this.setData(JSONObject.toJSONString(o));

    }

    public ApiResponseDataMessage(ApiResponseMessage apiResponseMessage,Object o) {
        this.setStatus( apiResponseMessage.getStatus());
        this.setMessage( apiResponseMessage.getMessage());
        this.setErrorCode(apiResponseMessage.getErrorCode());
        this.setErrorMessage(apiResponseMessage.getErrorMessage());
        this.setData(o);
    }
}
