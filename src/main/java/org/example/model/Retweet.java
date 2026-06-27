package org.example.model;

import java.time.ZonedDateTime;

public class Retweet {
    private String userId;
    private Long tweetId;
    private ZonedDateTime retweetedAt;

    public Retweet(){}

    public Retweet(String userId, Long tweetId, ZonedDateTime retweetedAt){
        this.userId = userId;
        this.tweetId = tweetId;
        this.retweetedAt = retweetedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public ZonedDateTime getRetweetedAt() {
        return retweetedAt;
    }

    public void setRetweetedAt(ZonedDateTime retweetedAt) {
        this.retweetedAt = retweetedAt;
    }
}
