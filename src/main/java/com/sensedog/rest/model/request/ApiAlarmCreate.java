package com.sensedog.rest.model.request;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class ApiAlarmCreate {

    @NotBlank
    private String cloudToken;
    @NotBlank
    private String deviceModel;
    @NotBlank
    private String osVersion;
    @NotBlank
    private String appVersion;
    @NotNull
    private Float battery;
    @NotBlank
    private String serviceName;
    private String carrier;

    public String getCloudToken() {
        return cloudToken;
    }

    public void setCloudToken(final String cloudToken) {
        this.cloudToken = cloudToken;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(final String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(final String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(final String appVersion) {
        this.appVersion = appVersion;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(final String carrier) {
        this.carrier = carrier;
    }

    public Float getBattery() {
        return battery;
    }

    public void setBattery(final Float battery) {
        this.battery = battery;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(final String serviceName) {
        this.serviceName = serviceName;
    }
}
