package com.sixj.dispatcher.common;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

/**
 * 基础异常
 * @author sixiaojie
 * @date 2020-09-03-17:41
 */
public class BaseException extends RuntimeException {
    private int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
    private Object[] params;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public BaseException() {
    }

    public BaseException(String message, int status) {
        super(message);
        this.status = status;
    }

    private BaseException(String message, String status) {
        super(message);
        this.status = Integer.parseInt(status);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    private BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.status = code;
    }

    public BaseException(BaseException result) {
        super(result.getMessage());
        status = result.getStatus();
    }

    public BaseException(BaseException result, Throwable cause) {
        this(result.getStatus(), result.getMessage(), cause);
    }

    public BaseException(BaseException result, Object... params) {
        this(MessageFormat.format(result.getMessage(), params), result.getStatus());
        this.params = params;
    }

    public BaseException(BaseException result, Throwable cause, Object... params) {
        this(result.getStatus(), MessageFormat.format(result.getMessage(), params), cause);
        this.params = params;
    }
}

