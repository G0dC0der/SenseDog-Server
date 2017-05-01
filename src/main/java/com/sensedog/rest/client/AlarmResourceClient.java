package com.sensedog.rest.client;

import com.sensedog.rest.entry.request.AlarmCreateRequest;
import com.sensedog.rest.entry.request.DetectRequest;
import com.sensedog.rest.entry.response.ServiceCreateResponse;
import com.sensedog.rest.entry.response.SeverityResponse;
import com.sensedog.rest.entry.response.TokenResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AlarmResourceClient {

    private String baseUrl;
    private Client client;

    public AlarmResourceClient(String baseUrl) {
        this.baseUrl = baseUrl;
        client = ClientBuilder.newClient();
    }

    public RestResponse<ServiceCreateResponse> create(AlarmCreateRequest request) {
        Response response = client.target(baseUrl)
                .path("alarm")
                .path("create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, ServiceCreateResponse.class);
    }

    public RestResponse<SeverityResponse> detection(String alarmAuthToken, DetectRequest request) {
        Response response = client.target(baseUrl)
                .path("alarm")
                .path("detection")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("alarm-auth-token", alarmAuthToken)
                .post(Entity.json(request));

        return RestResponse.fromResponse(response, SeverityResponse.class);
    }
}
