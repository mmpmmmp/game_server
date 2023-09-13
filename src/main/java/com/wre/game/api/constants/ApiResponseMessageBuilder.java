package com.wre.game.api.constants;


public class ApiResponseMessageBuilder {
    public static ApiResponseMessage build(RtCode rtCode) {
        return new ApiResponseMessage(rtCode);
    }
}
