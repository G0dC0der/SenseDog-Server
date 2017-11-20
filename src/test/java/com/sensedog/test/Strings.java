package com.sensedog.test;

import java.util.Random;

public class Strings {

    private static final char[] ALPHABET = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private static final char[] NUMBERS = {'0','1','2','3','4','5','6','7','8','9'};
    private static final Random RANDOM = new Random();

    public static String junk() {
        final int size = 10;
        final StringBuilder bu = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            bu.append(ALPHABET[RANDOM.nextInt(ALPHABET.length)]);
        }

        return bu.toString();
    }

    public static String numbers() {
        final int size = 10;
        final StringBuilder bu = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            bu.append(NUMBERS[RANDOM.nextInt(NUMBERS.length)]);
        }

        return bu.toString();
    }
}
