package com.sensedog.rest.model.request;

import org.hibernate.validator.constraints.NotBlank;

public class ApiConnect {

    @NotBlank
    private String pinCode;
    @NotBlank
    private String email;

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(final String pinCode) {
        this.pinCode = pinCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
