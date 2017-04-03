package com.sensedog.system;

public enum SystemStatus {

    STOPPED,
    ACTIVE;

    public boolean isStopped() {
        return this == STOPPED;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }
}
