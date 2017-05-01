package com.sensedog.rest.client;

import com.sensedog.rest.entry.response.ErrorResponse;

import javax.ws.rs.core.Response;

public class RestResponse<T> {

    public final int statusCode;
    public final T entity;
    public final ErrorResponse errorResponse;

    private RestResponse(int statusCode, T entity, ErrorResponse errorResponse) {
        this.statusCode = statusCode;
        this.entity = entity;
        this.errorResponse = errorResponse;
    }

    public boolean isOk() {
        return statusCode >= 200 &&  statusCode <= 299;
    }

    public boolean isRedirection() {
        return statusCode >= 300 &&  statusCode <= 399;
    }

    public boolean isClientError() {
        return statusCode >= 400 &&  statusCode <= 499;
    }

    public boolean isServerError() {
        return statusCode >= 500 &&  statusCode <= 599;
    }

    public static <T> RestResponse<T> fromResponse(Response resp, Class<T> clazz) {
        if(resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return new RestResponse<>(resp.getStatus(), resp.readEntity(clazz), null);
        } else {
            return new RestResponse<>(resp.getStatus(), null, resp.readEntity(ErrorResponse.class));
        }
    }
}