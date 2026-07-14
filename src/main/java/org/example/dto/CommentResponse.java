package org.example.dto;

import java.time.ZonedDateTime;

public class CommentResponse {
    private Long id;
    private Long tweetId;
    private UserResponse author;
    private String content;
    private Long parentCommentId;
    private ZonedDateTime createdAt;

    public CommentResponse() {
    }

    public CommentResponse(Long id, Long tweetId, UserResponse author, String content, Long parentCommentId,
            ZonedDateTime createdAt) {
        this.id = id;
        this.tweetId = tweetId;
        this.author = author;
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

    public void setAuthor(UserResponse author) {
        this.author = author;
    }

    public UserResponse getAuthor() {
        return author;
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
