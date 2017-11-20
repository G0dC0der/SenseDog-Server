package com.sensedog.rest.model;

public class ApiGenericResponse {

    private String message;

    public ApiGenericResponse() {
    }

    public ApiGenericResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}