package com.sensedog.rest.model.request;

import javax.validation.constraints.NotNull;

public class ApiHealthReport {

    @NotNull
    private Float battery;

    public Float getBattery() {
        return battery;
    }

    public void setBattery(final Float battery) {
        this.battery = battery;
    }
}
