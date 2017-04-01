package com.sensedog.rest.entry.response;

import java.util.Map;

public class ErrorResponse {

    private Integer statusCode;
    private String message;
    private Map<String, String> data;

    public ErrorResponse(Integer statusCode, String message, Map<String, String> data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
