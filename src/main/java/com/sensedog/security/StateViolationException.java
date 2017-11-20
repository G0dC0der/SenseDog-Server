package com.sensedog.security;

public class StateViolationException extends RuntimeException {

    public StateViolationException() {
    }

    public StateViolationException(final String message) {
        super(message);
    }

    public StateViolationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public StateViolationException(final Throwable cause) {
        super(cause);
    }

    public StateViolationException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
