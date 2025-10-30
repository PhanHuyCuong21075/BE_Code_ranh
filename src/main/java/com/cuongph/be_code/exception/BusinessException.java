package com.cuongph.be_code.exception;

public class BusinessException extends RuntimeException {
    private final int status;
    private final String code;

    public BusinessException(String message, String code, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
