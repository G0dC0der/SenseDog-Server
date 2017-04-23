package com.sensedog.rest;

import com.sensedog.repository.entry.Service;
import com.sensedog.rest.entry.request.ConnectRequest;
import com.sensedog.rest.entry.response.GenericResponse;
import com.sensedog.rest.entry.response.TokenResponse;
import com.sensedog.security.Token;
import com.sensedog.transmit.CloudClient;
import com.sensedog.security.SecurityManager;
import com.sensedog.transmit.CloudMessage;

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

@Path("/test")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestResource {

    @Inject
    private CloudClient cloudClient;

    @Inject
    private SecurityManager securityManager;

    @GET
    @Path("push")
    public Response push(@HeaderParam("alarm-auth-token") String authToken) {
        Service service = securityManager.authenticate(new Token.Alarm(authToken));
        String cloudToken = service.getAlarmDevice().getCloudToken();

        GenericResponse msg = new GenericResponse("hej hej");
        CloudMessage<GenericResponse> cloudMessage = CloudMessage.from(msg, cloudToken);

        cloudClient.send(cloudMessage);

        return Response.noContent().build();
    }
}
