package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class ApiException extends RuntimeException {


    private static final long serialVersionUID = -6456724051080170977L;
    private RtCode rtCode;

    public ApiException() {
        super();
    }

    public ApiException(Exception e) {
        super(e);
    }

    public ApiException(RtCode rtCode) {
        super(rtCode.getErrorMessage());
        this.rtCode = rtCode;
    }

    public ApiException(RtCode rtCode, Throwable throwable) {
        super(rtCode.getErrorMessage(), throwable);
        this.rtCode = rtCode;
    }

    public RtCode getRtCode() {
        return rtCode;
    }
}
