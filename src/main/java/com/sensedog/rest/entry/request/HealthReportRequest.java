package com.sensedog.rest.entry.request;

import javax.validation.constraints.NotNull;

public class HealthReportRequest {

    @NotNull
    private Float battery;

    public Float getBattery() {
        return battery;
    }

    public void setBattery(Float battery) {
        this.battery = battery;
    }
}
