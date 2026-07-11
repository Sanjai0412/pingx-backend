package org.example.service;

public interface FollowService {
    boolean followUser(String followerId, String followingId);
    boolean unfollowUser(String followerId, String followingId);
    boolean isFollowing(String followerId, String followingId);
    long getFollowersCount(String userId);
    long getFollowingCount(String userId);
}
