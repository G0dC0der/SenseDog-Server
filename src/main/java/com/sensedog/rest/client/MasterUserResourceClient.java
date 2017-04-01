package com.sensedog.rest.client;

import com.sensedog.rest.entry.request.ConnectRequest;
import com.sensedog.rest.entry.request.InviteRequest;
import com.sensedog.rest.entry.request.MasterUserCreateRequest;
import com.sensedog.rest.entry.response.TokenResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MasterUserResourceClient {

    private String baseUrl;
    private Client client;

    public MasterUserResourceClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<Void> newMasterUser(MasterUserCreateRequest request) {
        Response response = client.target(baseUrl)
                .path("master")
                .path("new")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, Void.class);
    }

    public RestResponse<TokenResponse> connect(ConnectRequest request) {
        Response response = client.target(baseUrl)
                .path("master")
                .path("connect")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, TokenResponse.class);
    }

    public RestResponse<Void> invite(String masterAuthToken, InviteRequest request) {
        Response response = client.target(baseUrl)
                .path("master")
                .path("invite")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("master-auth-token", masterAuthToken)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, Void.class);
    }
}
