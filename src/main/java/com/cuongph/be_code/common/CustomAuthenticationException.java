package com.cuongph.be_code.common;

public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
