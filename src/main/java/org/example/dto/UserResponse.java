package org.example.dto;

public class UserResponse {
    private String userId;
    private String username;
    private String displayName;
    private String profileImgUrl;

    public UserResponse() {
    }

    public UserResponse(String userId, String username, String displayName, String profileImgUrl) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.profileImgUrl = profileImgUrl;
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

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

}
