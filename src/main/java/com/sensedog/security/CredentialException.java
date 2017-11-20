package com.sensedog.security;

public class CredentialException extends RuntimeException {

    public CredentialException() {
    }

    public CredentialException(final String message) {
        super(message);
    }

    public CredentialException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CredentialException(final Throwable cause) {
        super(cause);
    }

    public CredentialException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
