package com.sensedog.rest.model.request;

import com.sensedog.detection.DetectionType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class ApiDetect {

    @NotNull
    private DetectionType detectionType;
    @NotBlank
    private String value;

    public DetectionType getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(final DetectionType detectionType) {
        this.detectionType = detectionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
