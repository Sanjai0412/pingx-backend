package org.example.dto;

import java.time.ZonedDateTime;

public class FeedResponse {
    private FeedType type;
    private UserResponse performedBy;
    private TweetResponse tweet;
    private ZonedDateTime activityAt;

    public FeedResponse() {
    }

    public FeedResponse(FeedType type, UserResponse performedBy, TweetResponse tweet, ZonedDateTime activityAt) {
        this.type = type;
        this.performedBy = performedBy;
        this.tweet = tweet;
        this.activityAt = activityAt;
    }

    public FeedType getType() {
        return type;
    }

    public UserResponse getPerformedBy() {
        return performedBy;
    }

    public TweetResponse getTweet() {
        return tweet;
    }

    public ZonedDateTime getActivityAt() {
        return activityAt;
    }

    public void setType(FeedType type) {
        this.type = type;
    }

    public void setPerformedBy(UserResponse performedBy) {
        this.performedBy = performedBy;
    }

    public void setTweet(TweetResponse tweet) {
        this.tweet = tweet;
    }

    public void setActivityAt(ZonedDateTime activityAt) {
        this.activityAt = activityAt;
    }

}
