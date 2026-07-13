package org.example.model;

import java.time.ZonedDateTime;

public class Tweet {
    private Long id;
    private String userId;
    private String content;
    private Long quoteTweetId;
    private ZonedDateTime createdAt;

    public Tweet() {
    };

    public Tweet(Long id, String userId, String content, Long quoteTweetId, ZonedDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.quoteTweetId = quoteTweetId;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getQuoteTweetId() {
        return this.quoteTweetId;
    }

    public void setQuoteTweetId(Long quoteTweetId) {
        this.quoteTweetId = quoteTweetId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
