package org.example.repository;

import java.util.List;

import org.example.dto.TweetResponse;
import org.example.model.Tweet;

public interface TweetRepository {
    TweetResponse createTweet(Tweet tweet, String username);

    List<TweetResponse> getAllTweets(String userId);

    Tweet getTweetById(Long tweetId);

    List<Tweet> getTweetsByUserId(String userId);
}
