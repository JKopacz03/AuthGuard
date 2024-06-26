package com.authguard.AuthGuard.exceptions;

public class NotExistingUserException extends RuntimeException {
    public NotExistingUserException() {
    }

    public NotExistingUserException(String message) {
        super(message);
    }

    public NotExistingUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingUserException(Throwable cause) {
        super(cause);
    }

    public NotExistingUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
