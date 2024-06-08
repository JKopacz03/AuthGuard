package com.authguard.AuthGuard.exceptions;

public class NotExistingPermissionException extends RuntimeException {
    public NotExistingPermissionException() {
    }

    public NotExistingPermissionException(String message) {
        super(message);
    }

    public NotExistingPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistingPermissionException(Throwable cause) {
        super(cause);
    }

    public NotExistingPermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
