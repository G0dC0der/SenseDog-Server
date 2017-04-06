package com.sensedog.rest;

import com.sensedog.detection.Severity;
import com.sensedog.rest.entry.request.AlarmCreateRequest;
import com.sensedog.rest.entry.request.DetectRequest;
import com.sensedog.rest.entry.response.SeverityResponse;
import com.sensedog.rest.entry.response.TokenResponse;
import com.sensedog.security.Token;
import com.sensedog.service.AlarmService;
import org.apache.http.auth.AuthenticationException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/alarm")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AlarmResource {

    private final AlarmService alarmService;

    @Inject
    public AlarmResource(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @POST
    @Path("create")
    public Response create(@Valid AlarmCreateRequest request) {
        String token = alarmService.create(request.getCloudToken(),
                                           request.getDeviceModel(),
                                           request.getOsVersion(),
                                           request.getAppVersion(),
                                           request.getCarrier(),
                                           request.getBattery());
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);

        return Response.ok(tokenResponse).build();
    }

    @POST
    @Path("detection")
    public Response detect(@Valid DetectRequest request,
                           @HeaderParam("alarm-auth-token") String authToken) throws AuthenticationException {
        Severity severity = alarmService.detect(new Token.Alarm(authToken), request.getDetectionType(), request.getValue());

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
}
