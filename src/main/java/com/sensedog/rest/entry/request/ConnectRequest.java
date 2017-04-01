package com.sensedog.rest.entry.request;

import org.hibernate.validator.constraints.NotBlank;

public class ConnectRequest {

    @NotBlank
    private String pinCode;
    @NotBlank
    private String email;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
