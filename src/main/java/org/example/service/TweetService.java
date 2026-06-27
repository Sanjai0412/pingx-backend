package org.example.service;

import org.example.dto.TweetResponse;
import org.example.model.Tweet;

import java.util.List;

public interface TweetService {
    TweetResponse postNewTweet(Tweet tweet, String username);
    List<TweetResponse> getAllTweets(String userId);
    Tweet getTweetById(Long tweetId);
    List<Tweet> getTweetsByUserId(String userId);
}
