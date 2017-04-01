package com.sensedog.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatusResource {

//    @POST
//    @Path("report")
//    public Response report() { //Battery, alarm token
//
//    }
//
//    @POST
//    @Path("view")
//    public Response view() { //master token
//        //return battery, last seen and status
//    }
}
