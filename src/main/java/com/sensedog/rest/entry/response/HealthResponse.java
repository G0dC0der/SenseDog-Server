package com.sensedog.rest.entry.response;

import com.sensedog.system.SystemStatus;

import java.time.ZonedDateTime;

public class HealthResponse {

    private Float battery;
    private ZonedDateTime lastSeen;
    private SystemStatus systemStatus;

    public Float getBattery() {
        return battery;
    }

    public void setBattery(Float battery) {
        this.battery = battery;
    }

    public ZonedDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(ZonedDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public SystemStatus getSystemStatus() {
        return systemStatus;
    }

    public void setSystemStatus(SystemStatus systemStatus) {
        this.systemStatus = systemStatus;
    }
}
