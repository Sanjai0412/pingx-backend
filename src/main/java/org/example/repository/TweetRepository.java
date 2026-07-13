package org.example.repository;

import java.util.List;

import org.example.dto.TweetResponse;
import org.example.model.Tweet;

public interface TweetRepository {
    TweetResponse createTweet(Tweet tweet);

    Tweet fetchTweetById(Long tweetId);

    TweetResponse createQuoteTweet(Tweet tweet);

    List<Long> getAllTweetIds();

    TweetResponse getTweetById(Long tweetId, String userId);

    List<Long> getTweetIdsByUserId(String targetUserId);
}
