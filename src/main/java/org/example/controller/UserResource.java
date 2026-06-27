package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.config.Secured;
import org.example.model.Tweet;
import org.example.model.User;
import org.example.model.ApiResponse;
import org.example.service.TweetService;
import org.example.service.UserService;

import java.util.List;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private final UserService userService;
    private final TweetService tweetService;

    // HK2 reads this annotation and passes in the UserServiceImpl obj
    @Inject
    public UserResource(UserService userService, TweetService tweetService){
        this.userService = userService;
        this.tweetService = tweetService;
    }
    @Secured
    @POST
    @Path("/")
    public Response saveUserProfile(User user, @Context ContainerRequestContext context){
        try{
            // get the userId from req body, which is from jwt access token
            String userId = (String) context.getProperty("userId");

            user.setUserId(userId);
            userService.registerUser(user);
            return Response.status(201)
                    .entity(new ApiResponse<User>(true, "User registered successfully", user))
                    .build();
        }catch (IllegalArgumentException e){
            return Response.status(400)
                    .entity(new ApiResponse<>(false, e.getMessage(), "INVALID_USER_CREDENTIALS"))
                    .build();
        }
    }

    @GET
    @Path("/{userId}")
    public Response getUserByUsername(@PathParam("userId") String userId){
        try{
            User user = userService.getUserProfileById(userId);
            return Response.status(200)
                    .entity(new ApiResponse<User>(true, "User fetched successfully", user))
                    .build();
        }catch (IllegalArgumentException e){
            return Response.status(404)
                    .entity(new ApiResponse<>(false, e.getMessage(), "INVALID_USER"))
                    .build();
        }
    }

    @GET
    @Path("/{userId}/tweets")
    public Response getUserTweets(@PathParam("userId") String userId){
        try{
            List<Tweet> tweets = tweetService.getTweetsByUserId(userId);
            return Response.status(200)
                    .entity(new ApiResponse<List<Tweet>>(true, "Tweets fetched successfully", tweets))
                    .build();
        }catch (IllegalArgumentException e){
            return Response.status(403)
                    .entity(new ApiResponse<>(false, e.getMessage(), "INVALID_USER_ID"))
                    .build();
        }
    }
}
