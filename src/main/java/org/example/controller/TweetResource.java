package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.config.Secured;
import org.example.dto.TweetResponse;
import org.example.exception.ConflictException;
import org.example.model.ApiResponse;
import org.example.model.Tweet;
import org.example.service.LikeService;
import org.example.service.RetweetService;
import org.example.service.TweetService;

import java.util.List;

@Path("/tweets")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TweetResource {
    private final TweetService tweetService;
    private final LikeService likeService;
    private final RetweetService retweetService;

    @Inject
    public TweetResource(TweetService tweetService, LikeService likeService, RetweetService retweetService) {
        this.tweetService = tweetService;
        this.likeService = likeService;
        this.retweetService = retweetService;
    }

    @Secured
    @POST
    @Path("/")
    public Response postTweet(Tweet tweet, @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            String username = (String) context.getProperty("username");
            tweet.setUserId(userId);
            TweetResponse createdTweet = tweetService.postNewTweet(tweet, username);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweet posted successfully", createdTweet))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        }
    }

    @Secured
    @GET
    @Path("/")
    public Response getAllTweets(@Context ContainerRequestContext context) {
        try {
            // get the userId from req body, which is from jwt access token
            String userId = (String) context.getProperty("userId");
            List<TweetResponse> tweet = tweetService.getAllTweets(userId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweet fetched successfully", tweet))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "NO_TWEETS"))
                    .build();
        }
    }

    @GET
    @Path("/{tweetId}")
    public Response getTweetById(@PathParam("tweetId") Long tweetId) {
        try {
            Tweet tweet = tweetService.getTweetById(tweetId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweet fetched successfully", tweet))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_TWEET_ID"))
                    .build();
        }
    }

    @Secured
    @POST
    @Path("/{tweetId}/like")
    public Response likeTweet(@PathParam("tweetId") Long tweetId, @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            boolean isLiked = likeService.likeTweet(userId, tweetId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweet liked successfully", isLiked))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "NOT_FOUND"))
                    .build();
        } catch (ConflictException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error(e.getMessage(), "ALREADY_LIKED"))
                    .build();
        }
    }

    @Secured
    @DELETE
    @Path("/{tweetId}/like")
    public Response unLikeTweet(@PathParam("tweetId") Long tweetId, @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            boolean isLiked = likeService.unlikeTweet(userId, tweetId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweet un-liked successfully", isLiked))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "NOT_FOUND"))
                    .build();
        } catch (ConflictException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error(e.getMessage(), "NOT_LIKED"))
                    .build();
        }
    }

    @Secured
    @POST
    @Path("/{tweetId}/retweet")
    public Response retweet(@PathParam("tweetId") Long tweetId, @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            boolean isRetweeted = retweetService.retweet(userId, tweetId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweet retweeted successfully", isRetweeted))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "NOT_FOUND"))
                    .build();
        } catch (ConflictException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error(e.getMessage(), "ALREADY_RETWEETED"))
                    .build();
        }
    }

    @Secured
    @DELETE
    @Path("/{tweetId}/retweet")
    public Response unRetweet(@PathParam("tweetId") Long tweetId, @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            boolean isRetweeted = retweetService.undoRetweet(userId, tweetId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweet un-retweeted successfully", isRetweeted))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "NOT_FOUND"))
                    .build();
        } catch (ConflictException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(ApiResponse.error(e.getMessage(), "NOT_RETWEETED"))
                    .build();
        }
    }
}
