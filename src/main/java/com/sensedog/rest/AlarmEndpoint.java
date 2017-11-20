package com.sensedog.rest;

import com.sensedog.detection.Severity;
import com.sensedog.rest.model.ApiHealth;
import com.sensedog.rest.model.request.ApiAlarmCreate;
import com.sensedog.rest.model.request.ApiCloudUpdate;
import com.sensedog.rest.model.request.ApiDetect;
import com.sensedog.rest.model.ApiServiceCreate;
import com.sensedog.rest.model.ApiSeverity;
import com.sensedog.rest.model.request.ApiHealthReport;
import com.sensedog.security.Token;
import com.sensedog.service.AlarmService;
import com.sensedog.service.StatusService;
import com.sensedog.service.UserService;
import com.sensedog.service.model.AlarmServiceInfo;
import com.sensedog.service.model.HealthStatus;
import org.apache.http.auth.AuthenticationException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/alarm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlarmEndpoint {

    private final AlarmService alarmService;
    private final UserService userService;
    private final StatusService statusService;

    @Inject
    public AlarmEndpoint(final AlarmService alarmService,
                         final UserService userService,
                         final StatusService statusService) {
        this.alarmService = alarmService;
        this.userService = userService;
        this.statusService = statusService;
    }

    @POST
    @Path("create")
    public Response create(@Valid final ApiAlarmCreate request) {
        final AlarmServiceInfo alarmServiceInfo = alarmService.create(
                request.getCloudToken(),
                request.getDeviceModel(),
                request.getOsVersion(),
                request.getAppVersion(),
                request.getCarrier(),
                request.getServiceName(),
                request.getBattery());

        final ApiServiceCreate response = new ApiServiceCreate();
        response.setAlarmAuthToken(alarmServiceInfo.getAlarmAuthToken());
        response.setPinCode(alarmServiceInfo.getPinCode());

        return Response.ok(response).build();
    }

    @POST
    @Path("detection")
    public Response detect(@Valid final ApiDetect request,
                           @HeaderParam("alarm-auth-token") final String authToken) throws AuthenticationException {
        final Severity severity = alarmService.detect(
                new Token.Alarm(authToken),
                request.getDetectionType(),
                request.getValue());

        final ApiSeverity response = new ApiSeverity();
        response.setSeverity(severity);

        return Response.ok(response).build();
    }

    @POST
    @Path("stop")
    public Response stop(@HeaderParam("alarm-auth-token") final String authToken) {
        alarmService.stop(new Token.Alarm(authToken));

        return Response.noContent().build();
    }

    @POST
    @Path("start")
    public Response start(@HeaderParam("alarm-auth-token") final String authToken) {
        alarmService.start(new Token.Alarm(authToken));

        return Response.noContent().build();
    }

    @POST
    @Path("destroy")
    public Response destroy(@HeaderParam("alarm-auth-token") final String authToken) {
        //TODO: Removes the entire service.
        //Client should also remove their auth token when this endpoint is called.
        return null;
    }

    @PUT
    @Path("update/cloud")
    public Response updateCloud(@HeaderParam("alarm-auth-token") final String authToken, final ApiCloudUpdate request) {
        userService.updateAlarmCloudToken(new Token.Alarm(authToken), request.getCloudToken());

        return Response.noContent().build();
    }

    @POST
    @Path("status")
    public Response report(@HeaderParam("alarm-auth-token") final String alarmAuthToken, @Valid final ApiHealthReport request) {
        statusService.report(new Token.Alarm(alarmAuthToken), request.getBattery());

        return Response.noContent().build();
    }

    @GET
    @Path("status")
    public Response readStatus(@HeaderParam("alarm-auth-token") final String alarmAuthToken) {
        final HealthStatus healthStatus = statusService.read(new Token.Alarm(alarmAuthToken));

        final ApiHealth response = new ApiHealth();
        response.setSystemStatus(healthStatus.getSystemStatus());

        return Response.ok(response).build();
    }
}
