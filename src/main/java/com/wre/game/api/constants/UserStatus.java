package com.wre.game.api.constants;

public enum UserStatus {
    ACTIVE(0),
    BLOCK(1);

    private final Integer value;

    private UserStatus(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }

}
