package org.example.model;

import java.time.ZonedDateTime;

public class Comment {
    private Long id;
    private Long tweetId;
    private String userId;
    private String content;
    private Long parentCommentId;
    private ZonedDateTime createdAt;

    public Comment() {
    }

    public Comment(Long id, Long tweetId, String userId, String content, Long parentCommentId,
            ZonedDateTime createdAt) {
        this.id = id;
        this.tweetId = tweetId;
        this.userId = userId;
        this.content = content;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}