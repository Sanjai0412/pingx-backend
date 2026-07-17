package org.example.service;

import org.example.dto.TweetResponse;
import org.example.model.Tweet;

import java.util.List;

public interface TweetService {
    TweetResponse postNewTweet(Tweet tweet);

    List<TweetResponse> getAllTweets(String userId);

    TweetResponse getTweetById(Long tweetId, String userId);

    List<TweetResponse> getTweetsByUserId(String currentUserId, String targetUserId);

    TweetResponse getRootTweet(Long tweetId, String currentUserId);

    TweetResponse buildTweetResponse(Long tweetId, String currentUserId, int depth);

    List<TweetResponse> buildTweetResponses(List<Long> tweetIds, String currentUserId, int depth);
}
