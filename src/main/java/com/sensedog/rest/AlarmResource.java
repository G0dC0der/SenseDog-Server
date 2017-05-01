package com.sensedog.rest;

import com.sensedog.detection.Severity;
import com.sensedog.rest.entry.request.AlarmCreateRequest;
import com.sensedog.rest.entry.request.CloudUpdateRequest;
import com.sensedog.rest.entry.request.DetectRequest;
import com.sensedog.rest.entry.response.ServiceCreateResponse;
import com.sensedog.rest.entry.response.SeverityResponse;
import com.sensedog.security.Token;
import com.sensedog.service.AlarmService;
import com.sensedog.service.UserService;
import com.sensedog.service.domain.ServiceInfo;
import org.apache.http.auth.AuthenticationException;

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

@Path("/alarm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlarmResource {

    private final AlarmService alarmService;
    private final UserService userService;

    @Inject
    public AlarmResource(final AlarmService alarmService,
                         final UserService userService) {
        this.alarmService = alarmService;
        this.userService = userService;
    }

    @POST
    @Path("create")
    public Response create(@Valid AlarmCreateRequest request) {
        ServiceInfo serviceInfo = alarmService.create(
                request.getCloudToken(),
                request.getDeviceModel(),
                request.getOsVersion(),
                request.getAppVersion(),
                request.getCarrier(),
                request.getServiceName(),
                request.getBattery());

        ServiceCreateResponse response = new ServiceCreateResponse();
        response.setAlarmAuthToken(serviceInfo.getAlarmAuthToken());
        response.setPinCode(serviceInfo.getPinCode());

        return Response.ok(response).build();
    }

    @POST
    @Path("detection")
    public Response detect(@Valid DetectRequest request,
                           @HeaderParam("alarm-auth-token") String authToken) throws AuthenticationException {
        Severity severity = alarmService.detect(
                new Token.Alarm(authToken),
                request.getDetectionType(),
                request.getValue());

        SeverityResponse response = new SeverityResponse();
        response.setSeverity(severity);

        return Response.ok(response).build();
    }

    @POST
    @Path("stop")
    public Response stop(@HeaderParam("alarm-auth-token") String authToken) {
        alarmService.stop(new Token.Alarm(authToken));

        return Response.noContent().build();
    }

    @POST
    @Path("start")
    public Response start(@HeaderParam("alarm-auth-token") String authToken) {
        alarmService.start(new Token.Alarm(authToken));

        return Response.noContent().build();
    }

    @POST
    @Path("destroy")
    public Response destroy(@HeaderParam("alarm-auth-token") String authToken) {
        //TODO: Removes the entire service.
        //Client should also remove their auth token when this endpoint is called.
        return null;
    }

    @PUT
    @Path("update/cloud")
    public Response updateCloud(@HeaderParam("alarm-auth-token") String authToken, CloudUpdateRequest request) {
        userService.updateAlarmCloudToken(new Token.Alarm(authToken), request.getCloudToken());

        return Response.noContent().build();
    }
}
