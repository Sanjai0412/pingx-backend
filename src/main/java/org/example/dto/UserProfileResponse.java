package org.example.dto;

import java.time.ZonedDateTime;

public class UserProfileResponse {
    private String userId;
    private String username;
    private String displayName;
    private String bio;
    private String profileImgUrl;
    private ZonedDateTime createdAt;
    private int followersCount;
    private int followingCount;
    private int postCount;

    public UserProfileResponse() {
    }

    public UserProfileResponse(String userId, String username, String displayName, String bio, String profileImgUrl,
            ZonedDateTime createdAt, int followersCount, int followingCount, int postCount) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.bio = bio;
        this.profileImgUrl = profileImgUrl;
        this.createdAt = createdAt;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.postCount = postCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getPostCount() {
        return this.postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
}
