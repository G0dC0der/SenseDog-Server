package com.sensedog.security;

public class AuthenticationFailedException extends RuntimeException {

    public AuthenticationFailedException() {
    }

    public AuthenticationFailedException(final String message) {
        super(message);
    }
}
