package com.sensedog.rest;

import com.sensedog.rest.entry.request.CloudUpdateRequest;
import com.sensedog.rest.entry.request.ConnectRequest;
import com.sensedog.rest.entry.request.InviteRequest;
import com.sensedog.rest.entry.request.MasterUserCreateRequest;
import com.sensedog.rest.entry.response.TokenResponse;
import com.sensedog.security.Token;
import com.sensedog.service.UserService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/master")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MasterResource {

    private final UserService userService;

    @Inject
    public MasterResource(final UserService userService) {
        this.userService = userService;
    }

    @POST
    @Path("new")
    public Response newMasterUser(@Valid MasterUserCreateRequest request) {
        userService.createMasterUser(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                request.getCloudToken());

        return Response.noContent().build();
    }

    @POST
    @Path("connect")
    public Response connect(@Valid ConnectRequest request) { //TODO: Nu kan vem som helst ansluta till en service. Vi måste verifera att personens email verkligen tillhör honom.
        String token = userService.connectToService(request.getPinCode(), request.getEmail());

        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);

        return Response.ok(tokenResponse).build();
    }

    @POST
    @Path("invite")
    public Response invite(@HeaderParam("master-auth-token") String authToken,
                           @Valid InviteRequest request) {
        userService.invite(
                new Token.Master(authToken),
                request.getName(),
                request.getPhone(),
                request.getEmail());

        return Response.noContent().build();
    }

    @POST
    @Path("resume")
    public Response resume(@HeaderParam("master-auth-token") String authToken) {
        userService.resume(new Token.Master(authToken));
        return Response.noContent().build();
    }

    @POST
    @Path("pause")
    public Response pause(@HeaderParam("master-auth-token") String authToken) {
        userService.pause(new Token.Master(authToken));
        return Response.noContent().build();
    }

    @PUT
    @Path("update/cloud")
    public Response updateCloud(@HeaderParam("master-auth-token") String authToken, CloudUpdateRequest request) {
        userService.updateMasterCloudToken(new Token.Master(authToken), request.getCloudToken());
        return Response.noContent().build();
    }
}
