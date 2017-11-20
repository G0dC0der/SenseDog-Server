package com.sensedog.util;

import com.sensedog.rest.model.ApiError;

import javax.ws.rs.core.Response;

public class RestResponse<T> {

    public final int statusCode;
    public final T entity;
    public final ApiError apiError;

    private RestResponse(final int statusCode, final T entity, final ApiError apiError) {
        this.statusCode = statusCode;
        this.entity = entity;
        this.apiError = apiError;
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

    public static <T> RestResponse<T> fromResponse(final Response resp, final Class<T> clazz) {
        if(resp.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            return new RestResponse<>(resp.getStatus(), resp.readEntity(clazz), null);
        } else {
            return new RestResponse<>(resp.getStatus(), null, resp.readEntity(ApiError.class));
        }
    }
}