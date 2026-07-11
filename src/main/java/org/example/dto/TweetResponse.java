package org.example.dto;

import java.time.ZonedDateTime;

public class TweetResponse {
    private Long id;
    private String username;
    private String profileImgUrl;
    private String displayName;
    private String userId;
    private String content;
    private int likeCount;
    private int retweetCount;
    // private boolean retweetedByCurrentUser;
    private boolean likedByCurrentUser;
    private ZonedDateTime createdAt;

    public TweetResponse() {
    }

    public TweetResponse(Long id, String username, String displayName, String userId, String content, int likeCount,
            int retweetCount,
            boolean likedByCurrentUser, ZonedDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.displayName = displayName;
        this.userId = userId;
        this.content = content;
        this.likeCount = likeCount;
        this.retweetCount = retweetCount;
        this.likedByCurrentUser = likedByCurrentUser;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getProfileImgUrl() {
        return this.profileImgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    // public boolean getRetweetedByCurrentUser() {
    // return retweetedByCurrentUser;
    // }

    // public void setRetweetedByCurrentUser(boolean retweetedByCurrentUser) {
    // this.retweetedByCurrentUser = retweetedByCurrentUser;
    // }

    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
