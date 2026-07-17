package org.example.service;

import jakarta.inject.Inject;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.model.NotificationType;
import org.example.repository.LikeRepository;
import org.example.repository.TweetRepository;

public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final NotificationService notificationService;

    @Inject
    public LikeServiceImpl(LikeRepository likeRepository, TweetRepository tweetRepository,
            NotificationService notificationService) {
        this.likeRepository = likeRepository;
        this.tweetRepository = tweetRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void likeTweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId, userId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        if (likeRepository.isLiked(userId, tweetId)) {
            throw new ConflictException("Tweet already liked");
        }
        likeRepository.likeTweet(userId, tweetId);

        String tweetOwnerId = tweetRepository.fetchTweetById(tweetId).getUserId();
        if (!tweetOwnerId.equals(userId)) {
            notificationService.notify(
                    tweetOwnerId,
                    userId,
                    NotificationType.LIKE,
                    tweetId,
                    null);
        }
    }

    @Override
    public void unlikeTweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (tweetRepository.getTweetById(tweetId, userId) == null) {
            throw new NotFoundException("Tweet not found");
        }
        if (!likeRepository.isLiked(userId, tweetId)) {
            throw new ConflictException("Tweet never liked");
        }
        likeRepository.unlikeTweet(userId, tweetId);

        String tweetOwnerId = tweetRepository.fetchTweetById(tweetId).getUserId();
        if (!tweetOwnerId.equals(userId)) {
            notificationService.deleteNotification(
                    tweetOwnerId,
                    userId,
                    NotificationType.LIKE,
                    tweetId,
                    null);
        }
    }

    @Override
    public boolean hasUserLikedTweet(String userId, Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        return likeRepository.isLiked(userId, tweetId);
    }

    @Override
    public long getLikeCount(Long tweetId) {
        if (tweetId == null) {
            throw new IllegalArgumentException("Tweet ID cannot be null");
        }
        return likeRepository.getLikesCount(tweetId);
    }
}
