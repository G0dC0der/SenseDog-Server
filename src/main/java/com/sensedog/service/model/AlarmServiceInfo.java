package com.sensedog.service.model;

public class AlarmServiceInfo {

    private String pinCode;
    private String alarmAuthToken;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(final String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAlarmAuthToken() {
        return alarmAuthToken;
    }

    public void setAlarmAuthToken(final String alarmAuthToken) {
        this.alarmAuthToken = alarmAuthToken;
    }
}
