package com.sensedog.rest.entry.response;

public class ServiceCreateResponse {

    private String pinCode;
    private String alarmAuthToken;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getAlarmAuthToken() {
        return alarmAuthToken;
    }

    public void setAlarmAuthToken(String alarmAuthToken) {
        this.alarmAuthToken = alarmAuthToken;
    }
}
