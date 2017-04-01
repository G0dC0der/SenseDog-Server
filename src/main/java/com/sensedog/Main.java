package com.sensedog;

import com.sensedog.system.SystemConstructor;

public class Main {

    public static void main(String... args) {
        SystemConstructor.constructServer().start();
    }
}
