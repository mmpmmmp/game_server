package com.wre.game.api.handler;

import com.wre.game.api.constants.ApiConstants;
import com.wre.game.api.constants.ApiResponseMessage;
import com.wre.game.api.constants.ApiResponseMessageBuilder;
import com.wre.game.api.constants.RtCode;
import com.wre.game.api.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;

@ControllerAdvice
public class ApiResponseEntityExceptionHandler {

    private static final Logger logger;
    private static final HttpHeaders defaultHttpHeaders;

    static {
        defaultHttpHeaders = new HttpHeaders();
        defaultHttpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        logger = LoggerFactory.getLogger(ApiResponseEntityExceptionHandler.class);
    }

    @ExceptionHandler(value = {Exception.class, ApiException.class})
    public final ResponseEntity<Object> handleApiException(Exception ex, WebRequest request) {

        RtCode rtCode = RtCode.RT_INTERNAL_ERROR;
        if (ex instanceof AuthException) {
            AuthException ae = (AuthException) ex;
            rtCode = ae.getRtCode();
        } else if (ex instanceof ParameterException) {
            logger.error(ex.getMessage(),ex);
            ParameterException pe = (ParameterException) ex;
            rtCode = pe.getRtCode();
        } else if (ex instanceof NotFoundException) {
            logger.error(ex.getMessage(),ex);
            NotFoundException nfe = (NotFoundException) ex;
            rtCode = nfe.getRtCode();
        } else if (ex instanceof ValidationException) {
            logger.error(ex.getMessage(),ex);
            ValidationException ve = (ValidationException) ex;
            rtCode = ve.getRtCode();
        } else if (ex instanceof WechatException) {
            logger.error(ex.getMessage(),ex);
            WechatException we = (WechatException) ex;
            rtCode = we.getRtCode();
        }

        ApiResponseMessage apiResponseMessage = ApiResponseMessageBuilder.build(rtCode);

        if(ex instanceof WechatException){
            WechatException we= (WechatException) ex;
            apiResponseMessage.setErrorMessage(we.getWechatErrorMessage());
        }else
        if (!(ex instanceof NotFoundException)) {
            logger.warn("Business Logic Error \nerrorCode : {}, errorMessage : {}", apiResponseMessage.getMessage(),
                    apiResponseMessage.getErrorCode(), ex);
        }
        logger.error(ex.getMessage(),ex);
        return new ResponseEntity<Object>(apiResponseMessage, defaultHttpHeaders, rtCode.getHttpStatus());
    }

    /**
     * Almost Exceptions from Controller layer extends SevletException.
     *
     * @param ex
     * @param request
     * @return ApiResponseMessage
     */
    @ExceptionHandler(value = {ServletException.class, TypeMismatchException.class, InvalidDataAccessApiUsageException.class})
    private ResponseEntity<Object> handleSpringInternalException(Exception ex, WebRequest request) {

        logger.warn("Spring Internal Error." + request.getDescription(false), ex);

        RtCode rtCode = RtCode.RT_INTERNAL_ERROR;
        StringBuilder sb = new StringBuilder();
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            rtCode = RtCode.RT_NOT_FOUND;
        } else if (ex instanceof ServletRequestBindingException || ex instanceof UnsatisfiedServletRequestParameterException) {
            rtCode = RtCode.RT_PARAMETER_ERROR;
            sb.append(rtCode.getErrorMessage()).append(ApiConstants.DELIMITER_HYPHEN).append(ex.getMessage());
        } else if (ex instanceof TypeMismatchException) {
            rtCode = RtCode.RT_PARAMETER_ERROR;
            TypeMismatchException tme = (TypeMismatchException) ex;
            sb.append("Requried Type").append("[").append(tme.getRequiredType()).append("]").append(ApiConstants.DELIMITER_COMMA)
                    .append(" Requested Parameter Value").append("[").append(tme.getValue()).append("]");
        } else if (ex instanceof InvalidDataAccessApiUsageException) {
            rtCode = RtCode.RT_PARAMETER_ERROR;
            InvalidDataAccessApiUsageException ida = (InvalidDataAccessApiUsageException) ex;
            sb.append(rtCode.getErrorMessage()).append(ApiConstants.DELIMITER_HYPHEN).append(ida.getCause().getMessage());
        }

        ApiResponseMessage apiResponseMessage = new ApiResponseMessage(rtCode);
        if (sb.length() > 0) {
            apiResponseMessage.setErrorMessage(sb.toString());
        }
        logger.error(ex.getMessage(),ex);
        return new ResponseEntity<Object>(apiResponseMessage, null, rtCode.getHttpStatus());
    }

}
