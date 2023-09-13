package com.wre.game.api.constants;

import java.io.Serializable;

public enum Channel implements Serializable {
    WECHAT("1"),
    OPPO("2");

    private final String value;

    private Channel(String tokenCode) {
        this.value = tokenCode;
    }

    public String tokenCode() {
        return this.value;
    }
}