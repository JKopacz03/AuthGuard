package com.authguard.AuthGuard.exceptions;

public class JwtTokenException extends RuntimeException {
    public JwtTokenException() {
    }

    public JwtTokenException(String invalidRefreshToken) {
    }

    public JwtTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtTokenException(Throwable cause) {
        super(cause);
    }

    public JwtTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
