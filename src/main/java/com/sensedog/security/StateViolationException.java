package com.sensedog.security;

public class StateViolationException extends RuntimeException {

    public StateViolationException() {
    }

    public StateViolationException(String message) {
        super(message);
    }

    public StateViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateViolationException(Throwable cause) {
        super(cause);
    }

    public StateViolationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
