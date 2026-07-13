package org.example.dto;

import jakarta.json.bind.annotation.JsonbTransient;

import java.time.ZonedDateTime;

public class TweetResponse {
    private Long id;
    private UserResponse author; // author's data
    private String content;
    private int likeCount;
    private int retweetCount;
    private boolean retweetedByCurrentUser;
    private boolean likedByCurrentUser;
    private ZonedDateTime createdAt;

    private TweetResponse quotedTweet; // quoted tweet (if this is a quote post)

    @JsonbTransient
    private Long quoteTweetId;

    public TweetResponse() {
    }

    public TweetResponse(Long id, UserResponse author, String content, int likeCount,
            int retweetCount, Long quotedTweetId, boolean retweetedByCurrentUser, boolean likedByCurrentUser,
            ZonedDateTime createdAt,
            TweetResponse quotedTweet) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.likeCount = likeCount;
        this.retweetCount = retweetCount;
        this.quoteTweetId = quotedTweetId;
        this.likedByCurrentUser = likedByCurrentUser;
        this.retweetedByCurrentUser = retweetedByCurrentUser;
        this.createdAt = createdAt;
        this.quotedTweet = quotedTweet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserResponse author) {
        this.author = author;
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

    public Long getQuoteTweetId() {
        return this.quoteTweetId;
    }

    public void setQuoteTweetId(Long quoteTweetId) {
        this.quoteTweetId = quoteTweetId;
    }

    public boolean isRetweetedByCurrentUser() {
        return retweetedByCurrentUser;
    }

    public void setRetweetedByCurrentUser(boolean retweetedByCurrentUser) {
        this.retweetedByCurrentUser = retweetedByCurrentUser;
    }

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

    public void setQuotedTweet(TweetResponse quotedTweet) {
        this.quotedTweet = quotedTweet;
    }

    public TweetResponse getQuotedTweet() {
        return this.quotedTweet;
    }
}
