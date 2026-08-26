package org.example.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/health")
public class HealthResource {

    @GET
    @Path("/")
    public Response checkHealth(){
        return Response.ok("PingX backend is healthy").build();
    }
}
