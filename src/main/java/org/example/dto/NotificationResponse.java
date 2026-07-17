package org.example.dto;

import java.time.ZonedDateTime;

import org.example.model.NotificationType;

public class NotificationResponse {
    private Long id;
    private NotificationType type;
    private UserResponse actor;
    private TweetResponse tweet;
    private CommentResponse comment;
    private boolean isRead;
    private ZonedDateTime createdAt;

    public NotificationResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public UserResponse getActor() {
        return actor;
    }

    public void setActor(UserResponse actor) {
        this.actor = actor;
    }

    public TweetResponse getTweet() {
        return tweet;
    }

    public void setTweet(TweetResponse tweet) {
        this.tweet = tweet;
    }

    public CommentResponse getComment() {
        return comment;
    }

    public void setComment(CommentResponse comment) {
        this.comment = comment;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
