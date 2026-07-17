package org.example.service;

import java.util.ArrayList;
import java.util.List;

import org.example.dto.NotificationResponse;
import org.example.model.Notification;
import org.example.model.NotificationType;
import org.example.repository.NotificationRepository;
import org.example.repository.TweetRepository;

import jakarta.inject.Inject;

public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;
    private final TweetRepository tweetRepository;

    @Inject
    public NotificationServiceImpl(NotificationRepository notificationRepository,
            TweetRepository tweetRepository,
            UserService userService) {
        this.notificationRepository = notificationRepository;
        this.tweetRepository = tweetRepository;
        this.userService = userService;
    }

    @Override
    public void createNotification(Notification notification) {

        // avoid notify theirself
        if (notification.getActorId().equals(notification.getRecipientId())) {
            return;
        }
        notificationRepository.createNotification(notification);
    }

    @Override
    public List<NotificationResponse> getNotificationsByRecipientId(String recipientId) {

        if (recipientId.isEmpty()) {
            throw new IllegalArgumentException("Recipient ID cannot be null");
        }
        List<Notification> notifications = notificationRepository.getNotificationsByRecipientId(recipientId);
        List<NotificationResponse> responses = new ArrayList<>();

        for (Notification notification : notifications) {
            NotificationResponse response = new NotificationResponse();

            response.setId(notification.getId());
            response.setType(notification.getType());
            response.setRead(notification.isRead());
            response.setCreatedAt(notification.getCreatedAt());

            response.setActor(
                    userService.getUserById(notification.getActorId()));

            if (notification.getTweetId() != null) {
                response.setTweet(
                        tweetRepository.getTweetById(notification.getTweetId(), recipientId));
            }
            if (notification.getReplyTweetId() != null) {
                response.setReplyTweet(
                        tweetRepository.getTweetById(notification.getReplyTweetId(), recipientId));
            }
            responses.add(response);
        }
        return responses;
    }

    @Override
    public void markAsRead(Long notificationId) {
        if (notificationId == null) {
            throw new IllegalArgumentException("Notification ID cannot be null");
        }
        notificationRepository.markAsRead(notificationId);
    }

    @Override
    public void markAllAsRead(String recipientId) {
        if (recipientId.isEmpty()) {
            throw new IllegalArgumentException("Recipient ID cannot be null");
        }
        notificationRepository.markAllAsRead(recipientId);
    }

    @Override
    public void deleteNotification(String recipientId, String actorId, NotificationType type, Long tweetId,
            Long replyTweetId) {

        notificationRepository.deleteNotification(
                recipientId,
                actorId,
                type,
                tweetId,
                replyTweetId);
    }

    @Override
    public void notify(
            String recipientId,
            String actorId,
            NotificationType type,
            Long tweetId,
            Long replyTweetId) {

        // Don't notify yourself
        if (recipientId.equals(actorId)) {
            return;
        }

        Notification notification = new Notification();

        notification.setRecipientId(recipientId);
        notification.setActorId(actorId);
        notification.setType(type);
        notification.setTweetId(tweetId);
        notification.setReplyTweetId(replyTweetId);

        notificationRepository.createNotification(notification);
    }

    @Override
    public int getUnreadNotificationCount(String recipientId) {
        if (recipientId.isEmpty()) {
            throw new IllegalArgumentException("Recipient ID cannot be empty");
        }
        return notificationRepository.getUnreadNotificationCount(recipientId);
    }
}
