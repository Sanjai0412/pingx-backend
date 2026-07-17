package org.example.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.config.Secured;
import org.example.dto.TweetResponse;
import org.example.dto.UserResponse;
import org.example.dto.UserProfileResponse;
import org.example.model.User;
import org.example.model.ApiResponse;
import org.example.service.FollowService;
import org.example.service.TweetService;
import org.example.service.UserService;
import org.example.service.ImageUploadService;
import org.glassfish.jersey.media.multipart.FormDataParam;
import java.io.InputStream;

import java.util.List;

@Path("/users")
public class UserResource {
    private final UserService userService;
    private final TweetService tweetService;
    private final FollowService followService;
    private final ImageUploadService imageUploadService;

    // HK2 reads this annotation and passes in the UserServiceImpl obj
    @Inject
    public UserResource(UserService userService, TweetService tweetService, FollowService followService,
            ImageUploadService imageUploadService) {
        this.userService = userService;
        this.tweetService = tweetService;
        this.followService = followService;
        this.imageUploadService = imageUploadService;
    }

    @Secured
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUserProfile(User user, @Context ContainerRequestContext context) {
        try {
            // get the userId from req body, which is from jwt access token
            String userId = (String) context.getProperty("userId");

            user.setUserId(userId);
            userService.registerUser(user);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("User registered successfully", user))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER_CREDENTIALS"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @Secured
    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserProfile(User user, @Context ContainerRequestContext context) {
        try {
            String userId = (String) context.getProperty("userId");
            user.setUserId(userId);
            UserResponse response = userService.updateUser(user);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("User profile updated successfully", response))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @GET
    @Path("/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(@PathParam("username") String username) {
        try {
            UserProfileResponse user = userService.getUserProfileByUsername(username);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("User fetched successfully", user))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @Secured
    @GET
    @Path("/{userId}/tweets")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTweets(@PathParam("userId") String targetUserId, @Context ContainerRequestContext context) {
        try {
            String currentUserId = (String) context.getProperty("userId");
            List<TweetResponse> tweets = tweetService.getTweetsByUserId(currentUserId, targetUserId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Tweets fetched successfully", tweets))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER_ID"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @Secured
    @POST
    @Path("/{userId}/follow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response followUser(@PathParam("userId") String userId, @Context ContainerRequestContext context) {
        try {
            String followerId = (String) context.getProperty("userId");

            followService.followUser(followerId, userId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("User followed successfully", null))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER_ID"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @Secured
    @DELETE
    @Path("/{userId}/unfollow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response unfollowUser(@PathParam("userId") String userId, @Context ContainerRequestContext context) {
        try {
            String followerId = (String) context.getProperty("userId");

            followService.unfollowUser(followerId, userId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("User unfollowed successfully", null))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER_ID"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @GET
    @Path("/{userId}/followers/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowersCount(@PathParam("userId") String userId) {
        try {
            long followersCount = followService.getFollowersCount(userId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Followers count fetched successfully", followersCount))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER_ID"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @GET
    @Path("/{userId}/following/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFollowingCount(@PathParam("userId") String userId) {
        try {
            long followingCount = followService.getFollowingCount(userId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Following count fetched successfully", followingCount))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER_ID"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @Secured
    @GET
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUsers(@QueryParam("q") String query, @Context ContainerRequestContext context) {
        try {
            String currentUserId = (String) context.getProperty("userId");
            List<User> matchedUsers = userService.searchUsers(query, currentUserId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Users searched successfully", matchedUsers))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_INPUT"))
                    .build();
        }
    }

    @Secured
    @GET
    @Path("/{userId}/is-following")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isFollowingUser(@PathParam("userId") String targetUserId,
            @Context ContainerRequestContext context) {
        try {
            String currentUserId = (String) context.getProperty("userId");
            boolean following = followService.isFollowing(currentUserId, targetUserId);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Follow status fetched successfully", following))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_USER_ID"))
                    .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error(e.getMessage(), "USER_NOT_FOUND"))
                    .build();
        }
    }

    @Secured
    @POST
    @Path("/upload-image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProfileImage(
            @FormDataParam("image") InputStream fileInputStream,
            @Context ContainerRequestContext context) {
        try {
            String imageUrl = imageUploadService.convertImageToUrl(fileInputStream);
            return Response.status(Response.Status.OK)
                    .entity(ApiResponse.success("Image uploaded successfully", imageUrl))
                    .build();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage(), "INVALID_IMAGE"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ApiResponse.error(e.getMessage(), "UPLOAD_FAILED"))
                    .build();
        }
    }
}
