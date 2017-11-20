package com.sensedog.rest.model.request;

import org.hibernate.validator.constraints.NotBlank;

public class ApiInvite {

    @NotBlank
    private String phone;
    @NotBlank
    private String email;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
