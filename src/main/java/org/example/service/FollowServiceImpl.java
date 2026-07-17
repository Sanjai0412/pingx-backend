package org.example.service;

import jakarta.inject.Inject;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.model.NotificationType;
import org.example.repository.FollowRepository;
import org.example.repository.UserRepository;

public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Inject
    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository,
            NotificationService notificationService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void followUser(String followerId, String followingId) {
        validateUserIds(followerId, followingId);

        if (followerId.equals(followingId)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }

        // Check if users exist
        if (userRepository.getUserById(followerId) == null) {
            throw new NotFoundException("Follower user not found");
        }
        if (userRepository.getUserById(followingId) == null) {
            throw new NotFoundException("Followed user not found");
        }

        // Check if already following
        if (followRepository.isFollowing(followerId, followingId)) {
            throw new ConflictException("Already following this user");
        }

        followRepository.followUser(followerId, followingId);

        if (!followingId.equals(followerId)) {
            notificationService.notify(
                    followingId,
                    followerId,
                    NotificationType.FOLLOW,
                    null,
                    null);
        }
    }

    @Override
    public void unfollowUser(String followerId, String followingId) {
        validateUserIds(followerId, followingId);

        // Check if users exist
        if (userRepository.getUserById(followerId) == null) {
            throw new NotFoundException("Follower user not found");
        }
        if (userRepository.getUserById(followingId) == null) {
            throw new NotFoundException("Followed user not found");
        }

        // Check if not following
        if (!followRepository.isFollowing(followerId, followingId)) {
            throw new ConflictException("Not following this user");
        }

        followRepository.unfollowUser(followerId, followingId);

        notificationService.deleteNotification(
                followingId,
                followerId,
                NotificationType.FOLLOW,
                null,
                null);
    }

    @Override
    public boolean isFollowing(String followerId, String followingId) {
        validateUserIds(followerId, followingId);
        return followRepository.isFollowing(followerId, followingId);
    }

    @Override
    public long getFollowersCount(String userId) {
        validateUserId(userId);
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        return followRepository.getFollowersCount(userId);
    }

    @Override
    public long getFollowingCount(String userId) {
        validateUserId(userId);
        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        return followRepository.getFollowingCount(userId);
    }

    private void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
    }

    private void validateUserIds(String followerId, String followingId) {
        if (followerId == null || followerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Follower ID is required");
        }
        if (followingId == null || followingId.trim().isEmpty()) {
            throw new IllegalArgumentException("Following ID is required");
        }
    }
}
