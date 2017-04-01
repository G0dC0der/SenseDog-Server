package com.sensedog.security;

public class ApiViolationException extends RuntimeException {

    public ApiViolationException() {
    }

    public ApiViolationException(String message) {
        super(message);
    }
}
