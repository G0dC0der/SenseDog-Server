package com.sensedog.service.model;

import com.sensedog.system.SystemStatus;

import java.time.ZonedDateTime;

public class HealthStatus {

    private Float battery;
    private ZonedDateTime lastSeen;
    private SystemStatus systemStatus;

    public SystemStatus getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(final SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }

    public Float getBattery() {
        return battery;
    }

    public void setBattery(final Float battery) {
        this.battery = battery;
    }

    public ZonedDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(final ZonedDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
}
