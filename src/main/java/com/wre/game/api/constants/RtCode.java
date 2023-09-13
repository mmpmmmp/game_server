package com.wre.game.api.constants;

import org.springframework.http.HttpStatus;

public enum RtCode {

    // 2XX SUCCESS Cases - Commons
    RT_SUCCESS("A0000", HttpStatus.OK, "Request Success"),
    RT_EXECUTED("A0001", HttpStatus.OK, "Request Executed"),

    // 4XX Error Cases
    RT_PARAMETER_ERROR("A4000", HttpStatus.BAD_REQUEST, "Parameter Error"),
    RT_NOT_FOUND("A4001", HttpStatus.NOT_FOUND, "Not Found Resource"),
    RT_NOT_FOUND_APP_ID("A4002", HttpStatus.NOT_FOUND, "AppId Not Found Resource"),
    UUID_NOT_MATCH("A4003",HttpStatus.UNAUTHORIZED,"uuid or token do not match"),
    USERID_NOT_MATCH("A4003",HttpStatus.UNAUTHORIZED,"uuid or token do not match"),
    RT_PARAMETER_ERROR_NICKNAME_AVATAR("A4110", HttpStatus.BAD_REQUEST, "nickName or avatarUrl parameter is mandatory"),
    RT_LOGIN_USER_NOT_FOUND("A4120", HttpStatus.NOT_FOUND, "Request User Not Found"),
    RT_LOGIN_USER_BLOCKED("A4121", HttpStatus.INTERNAL_SERVER_ERROR, "Request User is Blocked"),

    RT_TOKEN_INVALID("A4101", HttpStatus.UNAUTHORIZED, "Invalid Token"),
    RT_TOKEN_EXPIRED("A4102", HttpStatus.UNAUTHORIZED, "Token Expired"),
    RT_TOKEN_OLD("A4103", HttpStatus.UNAUTHORIZED, "Old Token"),
    RT_LOGIN_REQUIRED_CERTIFICATE("A4105", HttpStatus.UNAUTHORIZED, "Required Login"),

    // 5XX Error Cases Common
    RT_INTERNAL_ERROR("A5000", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error"),
    RT_LOGIN_WECHAT_ERROR("A5100", HttpStatus.INTERNAL_SERVER_ERROR, "Wechat API Error"),
    RT_LOGIN_QQ_ERROR("A5100", HttpStatus.INTERNAL_SERVER_ERROR, "QQ API Error"),
    RT_LOGIN_TOUTIAO_ERROR("A5101", HttpStatus.INTERNAL_SERVER_ERROR, "Toutiao API Error"),
    RT_LOGIN_O_ERROR("A5101", HttpStatus.INTERNAL_SERVER_ERROR, "API Error"),

    RT_PAYMENT_SUCCESS("A6200", HttpStatus.OK, "Payment Success"),
    RT_PAYMENT_APPLE_AUTH_ERROR("A6400", HttpStatus.UNAUTHORIZED, "Apple auth failed, return empty result"),
    RT_PAYMENT_FAILURE_TRANSACTION("A6410", HttpStatus.OK, "Transaction are empty"),
    RT_PAYMENT_FAILURE_TRANSACTION_ID("A6420", HttpStatus.OK, "Transaction id is not included in the payment"),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;
    private final String errorCode;

    private RtCode(String errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
