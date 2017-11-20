package com.sensedog.util.client;

import com.sensedog.rest.model.request.ApiConnect;
import com.sensedog.rest.model.request.ApiInvite;
import com.sensedog.rest.model.request.ApiMasterUserCreate;
import com.sensedog.rest.model.ApiServices;
import com.sensedog.rest.model.ApiToken;
import com.sensedog.util.RestResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class MasterUserResourceClient {

    private String baseUrl;
    private Client client;

    public MasterUserResourceClient(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.client = ClientBuilder.newClient();
    }

    public RestResponse<Void> newMasterUser(final ApiMasterUserCreate request) {
        final Response response = client.target(baseUrl)
                .path("master")
                .path("new")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, Void.class);
    }

    public RestResponse<ApiToken> connect(final ApiConnect request) {
        final Response response = client.target(baseUrl)
                .path("master")
                .path("connect")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, ApiToken.class);
    }

    public RestResponse<Void> invite(final String masterAuthToken, final ApiInvite request) {
        final Response response = client.target(baseUrl)
                .path("master")
                .path("invite")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("master-auth-token", masterAuthToken)
                .post(Entity.entity(request, MediaType.APPLICATION_JSON));

        return RestResponse.fromResponse(response, Void.class);
    }

    public RestResponse<ApiServices> viewAll(final String email) {
        final Response response = client.target(baseUrl)
                .path("master")
                .path("view")
                .path("all")
                .path(email)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        return RestResponse.fromResponse(response, ApiServices.class);
    }
}
