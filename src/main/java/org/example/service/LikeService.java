package org.example.service;

public interface LikeService {
    boolean likeTweet(String userId, Long tweetId);
    boolean unlikeTweet(String userId, Long tweetId);
    boolean hasUserLikedTweet(String userId, Long tweetId);
    long getLikeCount(Long tweetId);
}
