package org.example.repository;

public interface FollowRepository {
    boolean followUser(String followerId, String followingId);
    boolean unfollowUser(String followerId, String followingId);
    boolean isFollowing(String followerId, String followingId);

    long getFollowersCount(String userId);
    long getFollowingCount(String userId);
}
