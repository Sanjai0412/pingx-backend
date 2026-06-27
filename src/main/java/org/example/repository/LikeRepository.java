package org.example.repository;

public interface LikeRepository {
    boolean likeTweet(String userId, Long tweetId);

    boolean unlikeTweet(String userId, Long tweetId);

    boolean isLiked(String userId, Long tweetId);

    long getLikesCount(Long tweetId);
}
