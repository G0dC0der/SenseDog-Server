package com.sensedog.rest;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiIssuer;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.sensedog.detection.Severity;
import com.sensedog.rest.model.ApiHealth;
import com.sensedog.rest.model.ApiServiceCreate;
import com.sensedog.rest.model.ApiSeverity;
import com.sensedog.rest.model.request.ApiAlarmCreate;
import com.sensedog.rest.model.request.ApiCloudUpdate;
import com.sensedog.rest.model.request.ApiDetect;
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
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

@Api(
    name = "alarm_endpoint",
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

    @ApiMethod(name = "create_alarm_endpoint", path = "alarm/create", httpMethod = ApiMethod.HttpMethod.POST)
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

    @ApiMethod(name = "alarm_endpoint_detect", path = "alarm/detection", httpMethod = ApiMethod.HttpMethod.POST)
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

    @ApiMethod(name = "stop_alarm_endpoint", path = "alarm/stop", httpMethod = ApiMethod.HttpMethod.POST)
    public Response stop(@HeaderParam("alarm-auth-token") final String authToken) {
        alarmService.stop(new Token.Alarm(authToken));

        return Response.noContent().build();
    }

    @ApiMethod(name = "start_alarm_endpoint", path = "alarm/start", httpMethod = ApiMethod.HttpMethod.POST)
    public Response start(@HeaderParam("alarm-auth-token") final String authToken) {
        alarmService.start(new Token.Alarm(authToken));

        return Response.noContent().build();
    }

    @ApiMethod(name = "destroy_alarm_endpoint", path = "alarm/destroy", httpMethod = ApiMethod.HttpMethod.POST)
    public Response destroy(@HeaderParam("alarm-auth-token") final String authToken) {
        //TODO: Removes the entire service. Client should also remove their auth token when this endpoint is called.

        return null;
    }

    @ApiMethod(name = "alarm_endpoint_update_cloud_token", path = "alarm/update/cloud", httpMethod = ApiMethod.HttpMethod.POST)
    public Response updateCloud(@HeaderParam("alarm-auth-token") final String authToken, final ApiCloudUpdate request) {
        userService.updateAlarmCloudToken(new Token.Alarm(authToken), request.getCloudToken());

        return Response.noContent().build();
    }

    @ApiMethod(name = "put_alarm_endpoint_status", path = "alarm/status", httpMethod = ApiMethod.HttpMethod.POST)
    public Response report(@HeaderParam("alarm-auth-token") final String alarmAuthToken, @Valid final ApiHealthReport request) {
        statusService.report(new Token.Alarm(alarmAuthToken), request.getBattery());

        return Response.noContent().build();
    }

    @ApiMethod(name = "read_alarm_endpoint_status", path = "alarm/status", httpMethod = ApiMethod.HttpMethod.GET)
    public Response readStatus(@HeaderParam("alarm-auth-token") final String alarmAuthToken) {
        final HealthStatus healthStatus = statusService.read(new Token.Alarm(alarmAuthToken));

        final ApiHealth response = new ApiHealth();
        response.setSystemStatus(healthStatus.getSystemStatus());

        return Response.ok(response).build();
    }
}
