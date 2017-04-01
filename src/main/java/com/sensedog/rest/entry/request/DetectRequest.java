package com.sensedog.rest.entry.request;

import com.sensedog.detection.DetectionType;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class DetectRequest {

    @NotNull
    private DetectionType detectionType;
    @NotBlank
    private String value;

    public DetectionType getDetectionType() {
        return detectionType;
    }

    public void setDetectionType(DetectionType detectionType) {
        this.detectionType = detectionType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
