package com.sensedog;

import com.sensedog.system.EmbeddedServer;
import com.sensedog.system.SystemBootstrap;

public class Main {

    public static void main(final String... args) {
        EmbeddedServer.builder()
                .setPort(8080)
                .setContextPath("sensedog")
                .setResourceConfig(SystemBootstrap.class)
                .build()
                .start();
    }
}