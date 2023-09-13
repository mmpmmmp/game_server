package com.wre.game.api.exception;


import com.wre.game.api.constants.RtCode;

public class QqException extends ApiException {

    private String qqErrorMessage = "";

    public String getQqErrorMessage() {
        return qqErrorMessage;
    }

    public void setQqErrorMessage(String qqErrorMessage) {
        this.qqErrorMessage = qqErrorMessage;
    }

    public QqException() {
        super();
    }

    public QqException(RtCode rtCode) {
        super(rtCode);
    }

    public QqException(RtCode rtCode, Throwable throwable) {
        super(rtCode, throwable);
    }

    public QqException(RtCode rtCode, String qqErrorMessage) {
        super(rtCode);
        this.qqErrorMessage = qqErrorMessage;
    }
}
