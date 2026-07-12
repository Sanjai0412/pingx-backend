package org.example.service;

import jakarta.inject.Inject;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.repository.RetweetRepository;
import org.example.repository.TweetRepository;

public class RetweetServiceImpl implements RetweetService {
    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;

    @Inject
    public RetweetServiceImpl(RetweetRepository retweetRepository, TweetRepository tweetRepository) {
        this.retweetRepository = retweetRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public boolean retweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        if (retweetRepository.hasUserRetweetedTweet(userId, tweetId)) {
            throw new ConflictException("Tweet already retweeted");
        }
        return retweetRepository.retweetTweet(userId, tweetId);
    }

    @Override
    public boolean undoRetweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        if (!retweetRepository.hasUserRetweetedTweet(userId, tweetId)) {
            throw new ConflictException("Tweet not retweeted by this user");
        }
        return retweetRepository.undoRetweet(userId, tweetId);
    }

    @Override
    public boolean hasUserRetweetedTweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        return retweetRepository.hasUserRetweetedTweet(userId, tweetId);
    }

    @Override
    public long getRetweetCount(Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (tweetRepository.getTweetById(tweetId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        return retweetRepository.getRetweetsCount(tweetId);
    }

}
