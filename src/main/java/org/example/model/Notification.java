package org.example.model;

import java.time.ZonedDateTime;

public class Notification {
    private Long id;
    private String recipientId;
    private String actorId;
    private NotificationType type;
    private Long tweetId;
    private Long commentId;
    private boolean isRead;
    private ZonedDateTime createdAt;

    public Notification() {
    }

    public Notification(Long id, String recipientId, String actorId, NotificationType type, Long tweetId,
            Long commentId, boolean isRead, ZonedDateTime createdAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.actorId = actorId;
        this.type = type;
        this.tweetId = tweetId;
        this.commentId = commentId;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getActorId() {
        return actorId;
    }

    public void setActorId(String actorId) {
        this.actorId = actorId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
