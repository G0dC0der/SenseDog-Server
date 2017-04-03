package com.sensedog.rest;

import com.sensedog.rest.entry.request.HealthReportRequest;
import com.sensedog.rest.entry.response.HealthResponse;
import com.sensedog.security.Token;
import com.sensedog.service.StatusService;
import com.sensedog.service.domain.HealthStatus;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatusResource {

    private final StatusService statusService;

    @Inject
    public StatusResource(final StatusService statusService) {
        this.statusService = statusService;
    }

    @POST
    @Path("report")
    public Response report(@HeaderParam("alarm-auth-token") String alarmAuthToken, @Valid HealthReportRequest request) {
        statusService.report(new Token.Alarm(alarmAuthToken), request.getBattery());
        return Response.noContent().build();
    }

    @GET
    @Path("read")
    public Response read(@HeaderParam("master-auth-token") String masterAuthToken) {
        HealthStatus healthStatus = statusService.read(masterAuthToken);

        HealthResponse response = new HealthResponse();
        response.setBattery(healthStatus.getBattery());
        response.setLastSeen(healthStatus.getLastSeen());
        response.setSystemStatus(healthStatus.getSystemStatus());

        return Response.ok(response).build();
    }
}
