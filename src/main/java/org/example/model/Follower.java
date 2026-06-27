package org.example.model;

import java.time.ZonedDateTime;

public class Follower {
    private String followerId;
    private String followedId;
    private ZonedDateTime followedAt;

    public Follower(){}

    public Follower(String followerId, String followedId, ZonedDateTime followedAt){
        this.followerId = followerId;
        this.followedId = followedId;
        this.followedAt = followedAt;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFollowedId() {
        return followedId;
    }

    public void setFollowedId(String followedId) {
        this.followedId = followedId;
    }

    public ZonedDateTime getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(ZonedDateTime followedAt) {
        this.followedAt = followedAt;
    }
}
