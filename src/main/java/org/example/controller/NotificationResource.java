package org.example.controller;

import java.util.List;

import org.example.config.Secured;
import org.example.dto.NotificationResponse;
import org.example.model.ApiResponse;
import org.example.service.NotificationService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/notifications")
public class NotificationResource {
    private final NotificationService notificationService;

    @Inject
    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Secured
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotificationsByRecipientId(@Context ContainerRequestContext context) {
        String recipientId = (String) context.getProperty("userId");
        List<NotificationResponse> responses = notificationService.getNotificationsByRecipientId(recipientId);
        return Response.ok(ApiResponse.success("Notifications fetched successfully", responses)).build();
    }

    @Secured
    @POST
    @Path("/read")
    @Produces(MediaType.APPLICATION_JSON)
    public Response readAllNotifications(@Context ContainerRequestContext context) {
        String currentUserId = (String) context.getProperty("userId");
        notificationService.markAllAsRead(currentUserId);
        return Response.ok(ApiResponse.success("Notifications read successfully", null)).build();
    }

    @Secured
    @GET
    @Path("/unread-count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnreadNotificationCount(@Context ContainerRequestContext context) {
        String recipientId = (String) context.getProperty("userId");
        int count = notificationService.getUnreadNotificationCount(recipientId);
        return Response.ok(ApiResponse.success("Unread notification count fetched successfully", count)).build();
    }
}

