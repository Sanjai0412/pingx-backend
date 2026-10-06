package org.example.service;

public interface FollowService {
    void followUser(String followerId, String followingId);

    void unfollowUser(String followerId, String followingId);

    boolean isFollowing(String followerId, String followingId);
}
