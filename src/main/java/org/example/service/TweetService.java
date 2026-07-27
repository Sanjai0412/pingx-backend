package org.example.service;

import org.example.dto.TweetResponse;
import org.example.model.Tweet;

import java.util.List;

public interface TweetService {
    TweetResponse postNewTweet(Tweet tweet);

    TweetResponse getTweetById(Long tweetId, String userId);

    List<TweetResponse> getTweetsByUserId(String currentUserId, String targetUserId);

    TweetResponse buildTweetResponse(Long tweetId, String currentUserId, int depth);

    List<TweetResponse> buildTweetResponses(List<Long> tweetIds, String currentUserId, int depth);

    List<TweetResponse> getReplyTweetsByTweetId(Long tweetId, String currentUserId);

    TweetResponse replyTweet(Tweet tweet);

}
