package com.sensedog.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.sensedog.rest.convert.EntryConverter;
import com.sensedog.rest.model.ApiHealth;
import com.sensedog.rest.model.ApiServices;
import com.sensedog.rest.model.ApiToken;
import com.sensedog.rest.model.request.ApiCloudUpdate;
import com.sensedog.rest.model.request.ApiConnect;
import com.sensedog.rest.model.request.ApiInvite;
import com.sensedog.rest.model.request.ApiLogin;
import com.sensedog.rest.model.request.ApiMasterUserCreate;
import com.sensedog.security.Token;
import com.sensedog.service.StatusService;
import com.sensedog.service.UserService;
import com.sensedog.service.model.HealthStatus;
import com.sensedog.service.model.ServiceData;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Api(
    name = "master_endpoint",
    version = "v1",
    namespace =
    @ApiNamespace(
        ownerDomain = "sensedog.com",
        ownerName = "sensedog.com",
        packagePath = "com.sensedog.rest"
    ),
    issuers = {
        @ApiIssuer(
            name = "firebase",
            issuer = "https://securetoken.google.com/YOUR-PROJECT-ID",
            jwksUri = "https://www.googleapis.com/robot/v1/metadata/x509/securetoken@system.gserviceaccount.com")
    }
)
public class MasterEndpoint {

    private final UserService userService;
    private final StatusService statusService;

    @Inject
    public MasterEndpoint(final UserService userService,
                          final StatusService statusService) {
        this.userService = userService;
        this.statusService = statusService;
    }

    @ApiMethod(name = "master_endpoint_new", path = "master/new", httpMethod = ApiMethod.HttpMethod.POST)
    public Response newMasterUser(@Valid final ApiMasterUserCreate request) {
        userService.createMasterUser(
                request.getName(),
                request.getPhone(),
                request.getEmail(),
                request.getCloudToken());

        return Response.noContent().build();
    }

    @ApiMethod(name = "master_endpoint_login", path = "master/login", httpMethod = ApiMethod.HttpMethod.POST)
    public Response login(final ApiLogin request) {
        userService.login(request.getEmail());

        return Response.noContent().build();
    }

    //TODO: Currently anyone may join a service. We have to verify that the user has been invited.
    @ApiMethod(name = "master_endpoint_connect", path = "master/connect", httpMethod = ApiMethod.HttpMethod.POST)
    public Response connect(@Valid final ApiConnect request) {
        final String token = userService.connectToService(request.getPinCode(), request.getEmail());

        final ApiToken apiToken = new ApiToken();
        apiToken.setToken(token);

        return Response.ok(apiToken).build();
    }

    @ApiMethod(name = "master_endpoint_invite", path = "master/invite", httpMethod = ApiMethod.HttpMethod.POST)
    public Response invite(@HeaderParam("master-auth-token") final String authToken,
                           @Valid final ApiInvite request) {
        userService.invite(
                new Token.Master(authToken),
                request.getName(),
                request.getPhone(),
                request.getEmail());

        return Response.noContent().build();
    }

    @ApiMethod(name = "master_endpoint_view_all", path = "master/view/all/{email}", httpMethod = ApiMethod.HttpMethod.GET)
    public Response viewAll(@Named("email") final String email) {
        final List<ServiceData> serviceInfos = userService.viewAll(email);

        final ApiServices apiServices = new ApiServices();
        apiServices.setServices(serviceInfos.stream().map(EntryConverter::convert).collect(Collectors.toList()));

        return Response.ok(apiServices).build();
    }

    @ApiMethod(name = "master_endpoint_resume", path = "master/resume", httpMethod = ApiMethod.HttpMethod.POST)
    public Response resume(@HeaderParam("master-auth-token") final String authToken) {
        userService.resume(new Token.Master(authToken));

        return Response.noContent().build();
    }

    @ApiMethod(name = "master_endpoint_pause", path = "master/pause", httpMethod = ApiMethod.HttpMethod.POST)
    public Response pause(@HeaderParam("master-auth-token") final String authToken) {
        userService.pause(new Token.Master(authToken));
        return Response.noContent().build();
    }

    @ApiMethod(name = "master_endpoint_update_cloud_token", path = "master/update/cloud", httpMethod = ApiMethod.HttpMethod.POST)
    public Response updateCloud(@HeaderParam("master-auth-token") final String authToken, final ApiCloudUpdate request) {
        userService.updateMasterCloudToken(new Token.Master(authToken), request.getCloudToken());

        return Response.noContent().build();
    }

    @ApiMethod(name = "master_endpoint_status", path = "master/status", httpMethod = ApiMethod.HttpMethod.GET)
    public Response read(@HeaderParam("master-auth-token") final String masterAuthToken) {
        final HealthStatus healthStatus = statusService.read(new Token.Master(masterAuthToken));

        final ApiHealth response = new ApiHealth();
        response.setBattery(healthStatus.getBattery());
        response.setLastSeen(healthStatus.getLastSeen());
        response.setSystemStatus(healthStatus.getSystemStatus());

        return Response.ok(response).build();
    }
}