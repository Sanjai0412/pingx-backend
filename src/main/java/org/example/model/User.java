package org.example.model;

import java.time.ZonedDateTime;

public class User {
    private String userId;
    private String username;
    private String displayName;
    private String bio;
    private String profileImgUrl;
    private ZonedDateTime createdAt;

    public User(){} // for JSON serialization (Jackson)
    public User(String userId, String username, String displayName, String bio, String profileImgUrl, ZonedDateTime createdAt){
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.bio = bio;
        this.profileImgUrl = profileImgUrl;
        this.createdAt = createdAt;
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
        return createdAt;
    }
    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
