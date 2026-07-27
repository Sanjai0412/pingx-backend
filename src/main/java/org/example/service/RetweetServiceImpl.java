package org.example.service;

import jakarta.inject.Inject;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.model.NotificationType;
import org.example.repository.RetweetRepository;
import org.example.repository.TweetRepository;

public class RetweetServiceImpl implements RetweetService {
    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final NotificationService notificationService;

    @Inject
    public RetweetServiceImpl(RetweetRepository retweetRepository, TweetRepository tweetRepository,
            NotificationService notificationService) {
        this.retweetRepository = retweetRepository;
        this.tweetRepository = tweetRepository;
        this.notificationService = notificationService;
    }

    @Override
    public boolean retweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId, userId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        if (retweetRepository.hasUserRetweetedTweet(userId, tweetId)) {
            throw new ConflictException("Tweet already retweeted");
        }

        boolean result = retweetRepository.retweetTweet(userId, tweetId);
        if (result) {
            String tweetOwnerId = tweetRepository.fetchTweetById(tweetId).getUserId();
            notificationService.notify(
                    tweetOwnerId,
                    userId,
                    NotificationType.RETWEET,
                    tweetId,
                    null);
        }
        return result;
    }

    @Override
    public boolean undoRetweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId, userId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        if (!retweetRepository.hasUserRetweetedTweet(userId, tweetId)) {
            throw new ConflictException("Tweet not retweeted by this user");
        }

        boolean result = retweetRepository.undoRetweet(userId, tweetId);
        if (result) {
            String tweetOwnerId = tweetRepository.fetchTweetById(tweetId).getUserId();
            notificationService.deleteNotification(
                    tweetOwnerId,
                    userId,
                    NotificationType.RETWEET,
                    tweetId,
                    null);
        }
        return result;
    }

    @Override
    public boolean hasUserRetweetedTweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId, userId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        return retweetRepository.hasUserRetweetedTweet(userId, tweetId);
    }

}
