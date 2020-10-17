package com.wine.to.up.user.service.exception;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
