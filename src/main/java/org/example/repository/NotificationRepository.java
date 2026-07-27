package org.example.repository;

import java.util.List;

import org.example.model.Notification;
import org.example.model.NotificationType;

public interface NotificationRepository {
    void createNotification(Notification notification);;

    List<Notification> getNotificationsByRecipientId(String recipientId);

    void markAllAsRead(String recipientId);

    int getUnreadNotificationCount(String recipientId);

    void deleteNotification(String recipientId,
            String actorId,
            NotificationType type,
            Long tweetId,
            Long commentId);
}
