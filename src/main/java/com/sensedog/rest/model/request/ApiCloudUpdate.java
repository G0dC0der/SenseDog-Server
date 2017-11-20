package com.sensedog.rest.model.request;

import org.hibernate.validator.constraints.NotBlank;

public class ApiCloudUpdate {

    @NotBlank
    private String cloudToken;

    public String getCloudToken() {
        return cloudToken;
    }

    public void setCloudToken(final String cloudToken) {
        this.cloudToken = cloudToken;
    }
}
