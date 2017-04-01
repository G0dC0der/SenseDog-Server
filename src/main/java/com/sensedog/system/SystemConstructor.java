package com.sensedog.system;

public class SystemConstructor {

    public static EmbeddedServer constructServer() {
        return EmbeddedServer.builder()
                .setPort(8080)
                .setContextPath("dog")
                .setResourceConfig(SystemBootstrap.class)
                .build();
    }
}
