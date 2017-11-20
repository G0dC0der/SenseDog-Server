package com.sensedog.security;

import java.util.Random;
import java.util.UUID;

public class Cipher { //TODO: UNSAFE CLASS!

    private static final char[] CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final Random RANDOM = new Random();

    public static String authToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String pinCode() {
        final StringBuilder bu = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            bu.append(CHARS[RANDOM.nextInt(CHARS.length)]);
        }

        return bu.toString();
    }
}
