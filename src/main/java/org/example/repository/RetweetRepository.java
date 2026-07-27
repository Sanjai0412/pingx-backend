package org.example.repository;

public interface RetweetRepository {
    boolean retweetTweet(String userId, Long tweetId);
    boolean undoRetweet(String userId, Long tweetId);
    boolean hasUserRetweetedTweet(String userId, Long tweetId);
}
