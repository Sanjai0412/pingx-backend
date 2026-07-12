package org.example.service;

public interface RetweetService {
    boolean retweet(String userId, Long tweetId);

    boolean undoRetweet(String userId, Long tweetId);

    boolean hasUserRetweetedTweet(String userId, Long tweetId);

    long getRetweetCount(Long tweetId);
}
