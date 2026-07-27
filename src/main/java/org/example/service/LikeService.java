package org.example.service;

public interface LikeService {
    void likeTweet(String userId, Long tweetId);

    void unlikeTweet(String userId, Long tweetId);

    boolean hasUserLikedTweet(String userId, Long tweetId);
}
