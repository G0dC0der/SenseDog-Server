package com.sensedog.rest.model.request;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class ApiMasterUserCreate {

    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @Email
    private String email;
    @NotBlank
    private String cloudToken;

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

    public String getCloudToken() {
        return cloudToken;
    }

    public void setCloudToken(final String cloudToken) {
        this.cloudToken = cloudToken;
    }
}
