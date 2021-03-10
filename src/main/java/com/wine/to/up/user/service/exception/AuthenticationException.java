package com.wine.to.up.user.service.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Authentication failed");
    }

    public AuthenticationException(final String message) {
        super(message);
    }

    public AuthenticationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(final Throwable cause) {
        super(cause);
    }
}
