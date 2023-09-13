package com.wre.game.api.data.param;

public class GameCodeReqParam {

    String code;

    @Override
    public String toString() {
        return "GameCodeReqParam{" +
                "code='" + code + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
