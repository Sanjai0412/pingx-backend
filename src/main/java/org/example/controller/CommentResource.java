package org.example.controller;

import java.util.List;

import org.example.config.Secured;
import org.example.dto.CommentResponse;
import org.example.model.ApiResponse;
import org.example.model.Comment;
import org.example.service.CommentService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tweets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {
    private final CommentService commentService;

    @Inject
    public CommentResource(CommentService commentService) {
        this.commentService = commentService;
    }

    @Secured
    @POST
    @Path("/{tweetId}/comment")
    public Response postComment(@PathParam("tweetId") Long tweetId, Comment comment,
            @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            comment.setTweetId(tweetId);
            comment.setUserId(userId);
            CommentResponse createdComment = commentService.postComment(comment);
            System.out.println(createdComment);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Comment posted successfully", createdComment))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        }
    }

    @Secured
    @GET
    @Path("/{tweetId}/comment")
    public Response getCommentsByTweetId(@PathParam("tweetId") Long tweetId, @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            List<CommentResponse> comments = commentService.getCommentsByTweetId(tweetId, userId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Comments fetched successfully", comments))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        }
    }
}
