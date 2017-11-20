package com.sensedog.rest;

import com.sensedog.rest.convert.EntryConverter;
import com.sensedog.rest.model.ApiHealth;
import com.sensedog.rest.model.request.ApiCloudUpdate;
import com.sensedog.rest.model.request.ApiConnect;
import com.sensedog.rest.model.request.ApiInvite;
import com.sensedog.rest.model.request.ApiLogin;
import com.sensedog.rest.model.request.ApiMasterUserCreate;
import com.sensedog.rest.model.ApiServices;
import com.sensedog.rest.model.ApiToken;
import com.sensedog.security.Token;
import com.sensedog.service.StatusService;
import com.sensedog.service.UserService;
import com.sensedog.service.model.HealthStatus;
import com.sensedog.service.model.ServiceData;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/master")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MasterEndpoint {

    private final UserService userService;
    private final StatusService statusService;

    @Inject
    public MasterEndpoint(final UserService userService,
                          final StatusService statusService) {
        this.userService = userService;
        this.statusService = statusService;
    }

    @POST
    @Path("new")
    public Response newMasterUser(@Valid final ApiMasterUserCreate request) {
        userService.createMasterUser(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                request.getCloudToken());

        return Response.noContent().build();
    }

    @POST
    @Path("login")
    public Response login(final ApiLogin request) {
        userService.login(request.getEmail());

        return Response.noContent().build();
    }

    @POST
    @Path("connect")
    public Response connect(@Valid final ApiConnect request) { //TODO: Nu kan vem som helst ansluta till en service. Vi måste verifera att personens email verkligen tillhör honom.
        final String token = userService.connectToService(request.getPinCode(), request.getEmail());

        final ApiToken apiToken = new ApiToken();
        apiToken.setToken(token);

        return Response.ok(apiToken).build();
    }

    @POST
    @Path("invite")
    public Response invite(@HeaderParam("master-auth-token") final String authToken,
                           @Valid final ApiInvite request) {
        userService.invite(
                new Token.Master(authToken),
                request.getName(),
                request.getPhone(),
                request.getEmail());

        return Response.noContent().build();
    }

    @GET
    @Path("view/all/{email}")
    public Response viewAll(@PathParam("email") final String email) {
        final List<ServiceData> serviceInfos = userService.viewAll(email);

        final ApiServices apiServices = new ApiServices();
        apiServices.setServices(serviceInfos.stream().map(EntryConverter::convert).collect(Collectors.toList()));

        return Response.ok(apiServices).build();
    }

    @POST
    @Path("resume")
    public Response resume(@HeaderParam("master-auth-token") final String authToken) {
        userService.resume(new Token.Master(authToken));

        return Response.noContent().build();
    }

    @POST
    @Path("pause")
    public Response pause(@HeaderParam("master-auth-token") final String authToken) {
        userService.pause(new Token.Master(authToken));
        return Response.noContent().build();
    }

    @PUT
    @Path("update/cloud")
    public Response updateCloud(@HeaderParam("master-auth-token") final String authToken, final ApiCloudUpdate request) {
        userService.updateMasterCloudToken(new Token.Master(authToken), request.getCloudToken());

        return Response.noContent().build();
    }

    @GET
    @Path("status")
    public Response read(@HeaderParam("master-auth-token") final String masterAuthToken) {
        final HealthStatus healthStatus = statusService.read(new Token.Master(masterAuthToken));

        final ApiHealth response = new ApiHealth();
        response.setBattery(healthStatus.getBattery());
        response.setLastSeen(healthStatus.getLastSeen());
        response.setSystemStatus(healthStatus.getSystemStatus());

        return Response.ok(response).build();
    }
}