package com.sensedog.rest.client;

import com.sensedog.rest.model.request.ApiAlarmCreate;
import com.sensedog.rest.model.request.ApiDetect;
import com.sensedog.rest.model.ApiServiceCreate;
import com.sensedog.rest.model.ApiSeverity;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AlarmResourceClient {

    private String baseUrl;
    private Client client;

    public AlarmResourceClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        client = ClientBuilder.newClient();
    }

    public RestResponse<ApiServiceCreate> create(final ApiAlarmCreate request) {
        final Response response = client.target(baseUrl)
                .path("alarm")
                .path("create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, ApiServiceCreate.class);
    }

    public RestResponse<ApiSeverity> detection(final String alarmAuthToken, final ApiDetect request) {
        final Response response = client.target(baseUrl)
                .path("alarm")
                .path("detection")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("alarm-auth-token", alarmAuthToken)
                .post(Entity.json(request));

        return RestResponse.fromResponse(response, ApiSeverity.class);
    }
}
