package com.sensedog.rest.entry.request;

import org.hibernate.validator.constraints.NotBlank;

public class CloudUpdateRequest {

    @NotBlank
    private String cloudToken;

    public String getCloudToken() {
        return cloudToken;
    }

    public void setCloudToken(String cloudToken) {
        this.cloudToken = cloudToken;
    }
}
