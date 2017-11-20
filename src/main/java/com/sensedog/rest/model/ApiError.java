package com.sensedog.rest.model;

import java.util.Map;

public class ApiError {

    private Integer statusCode;
    private String message;
    private Map<String, String> data;

    public ApiError(final Integer statusCode, final String message, final Map<String, String> data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(final Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(final Map<String, String> data) {
        this.data = data;
    }
}
