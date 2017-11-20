package com.sensedog.rest.model;

import com.sensedog.detection.Severity;

public class ApiSeverity {

    private Severity severity;

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(final Severity severity) {
        this.severity = severity;
    }
}
