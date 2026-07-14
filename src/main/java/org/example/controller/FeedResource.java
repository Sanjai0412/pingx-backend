package org.example.controller;

import java.util.List;

import org.example.config.Secured;
import org.example.dto.FeedResponse;
import org.example.model.ApiResponse;
import org.example.service.FeedService;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("/feed")
public class FeedResource {
    private final FeedService feedService;

    @Inject
    public FeedResource(FeedService feedService) {
        this.feedService = feedService;
    }

    @GET
    @Secured
    @Path("/")
    public Response getFeed(
            @QueryParam("limit") @DefaultValue("20") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @Context ContainerRequestContext context) {

        try {
            String userId = (String) context.getProperty("userId");

            List<FeedResponse> feed = feedService.getHomeFeed(userId, limit, offset);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Feed fetched successfully", feed))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "NOT_FOUND"))
                    .build();
        }
    }
}
