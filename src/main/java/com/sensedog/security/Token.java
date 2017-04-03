package com.sensedog.security;

public class Token {

    public static class Alarm extends Token {
        public Alarm(String token) {
            super(token);
        }
    }

    public static class Master extends Token {
        public Master(String token) {
            super(token);
        }
    }

    private final transient String token;

    private Token(String token) {
        if (token == null || token.isEmpty()) {
            throw new CredentialException("Token is null.");
        }

        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
