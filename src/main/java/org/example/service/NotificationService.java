package org.example.service;

import java.util.List;

import org.example.dto.NotificationResponse;
import org.example.model.Notification;
import org.example.model.NotificationType;

public interface NotificationService {
        void createNotification(Notification notification);

        List<NotificationResponse> getNotificationsByRecipientId(String recipientId);

        void markAllAsRead(String recipientId);

        int getUnreadNotificationCount(String recipientId);

        void deleteNotification(
                        String recipientId,
                        String actorId,
                        NotificationType type,
                        Long tweetId,
                        Long commentId);

        // Helper function
        void notify(
                        String recipientId,
                        String actorId,
                        NotificationType type,
                        Long tweetId,
                        Long commentId);
}
