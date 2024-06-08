package com.authguard.AuthGuard.exceptions;

public class NotExistingRoleException extends RuntimeException {
    public NotExistingRoleException() {
    }

    public NotExistingRoleException(String message) {
        super(message);
    }

    public NotExistingRoleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingRoleException(Throwable cause) {
        super(cause);
    }

    public NotExistingRoleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
