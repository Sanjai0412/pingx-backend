package org.example.model;

import java.time.ZonedDateTime;

import org.example.dto.FeedType;

public class FeedActivity {
    private Long tweetId;
    private FeedType type;
    private String performedByUserId;
    private ZonedDateTime activityAt;

    public FeedActivity() {
    }

    public FeedActivity(Long tweetId, FeedType type, String performedByUserId, ZonedDateTime activityAt) {
        this.tweetId = tweetId;
        this.type = type;
        this.performedByUserId = performedByUserId;
        this.activityAt = activityAt;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public FeedType getType() {
        return type;
    }

    public void setType(FeedType type) {
        this.type = type;
    }

    public String getPerformedByUserId() {
        return performedByUserId;
    }

    public void setPerformedByUserId(String performedByUserId) {
        this.performedByUserId = performedByUserId;
    }

    public ZonedDateTime getActivityAt() {
        return activityAt;
    }

    public void setActivityAt(ZonedDateTime activityAt) {
        this.activityAt = activityAt;
    }

}
