package org.example.model;

import java.time.ZonedDateTime;

public class Like {
    private String userId;
    private Long tweetId;
    private ZonedDateTime likedAt;

    public Like(){}

    public Like(String userId, Long tweetId, ZonedDateTime likedAt){
        this.userId = userId;
        this.tweetId = tweetId;
        this.likedAt = likedAt;
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

    public ZonedDateTime getLikedAt() {
        return likedAt;
    }

    public void setLikedAt(ZonedDateTime likedAt) {
        this.likedAt = likedAt;
    }
}
