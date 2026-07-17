package org.example.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.example.config.DatabaseConnection;
import org.example.model.Notification;
import org.example.model.NotificationType;

public class NotificationRepositoryImpl implements NotificationRepository {

    @Override
    public void createNotification(Notification notification) {
        String sql = """
                    INSERT INTO notifications(recipient_id, actor_id, type, tweet_id, reply_tweet_id)
                    VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, notification.getRecipientId());
            preparedStatement.setString(2, notification.getActorId());
            preparedStatement.setString(3, notification.getType().toString());
            if (notification.getTweetId() != null) {
                preparedStatement.setLong(4, notification.getTweetId());
            } else {
                preparedStatement.setNull(4, java.sql.Types.BIGINT);
            }

            if (notification.getReplyTweetId() != null) {
                preparedStatement.setLong(5, notification.getReplyTweetId());
            } else {
                preparedStatement.setNull(5, java.sql.Types.BIGINT);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Notification> getNotificationsByRecipientId(String recipientId) {
        String sql = """
                    SELECT *
                    FROM notifications
                    WHERE recipient_id = ?
                    ORDER BY created_at DESC
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, recipientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Notification> notifications = new ArrayList<>();
                while (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.setId(resultSet.getLong("id"));
                    notification.setRecipientId(resultSet.getString("recipient_id"));
                    notification.setActorId(resultSet.getString("actor_id"));
                    notification.setType(NotificationType.valueOf(resultSet.getString("type")));
                    Object tweetId = resultSet.getObject("tweet_id");

                    if (tweetId != null) {
                        notification.setTweetId(((Number) tweetId).longValue());
                    }
                    Object replyTweetId = resultSet.getObject("reply_tweet_id");
                    if (replyTweetId != null) {
                        notification.setReplyTweetId(((Number) replyTweetId).longValue());
                    }
                    notification
                            .setCreatedAt(resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC")));
                    notification.setRead(resultSet.getBoolean("is_read"));
                    notifications.add(notification);
                }
                return notifications;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void markAsRead(Long notificationId) {
        String sql = "UPDATE notifications SET is_read = true WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, notificationId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void markAllAsRead(String recipientId) {
        String sql = "UPDATE notifications SET is_read = true WHERE recipient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, recipientId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteNotification(String recipientId,
            String actorId,
            NotificationType type,
            Long tweetId,
            Long replyTweetId) {
        StringBuilder sql = new StringBuilder(
                """
                                    DELETE FROM notifications
                                        WHERE recipient_id = ?
                                        AND actor_id = ?
                                        AND type = ?
                        """);

        if (tweetId != null) {
            sql.append(" AND tweet_id = ?");
        } else {
            sql.append(" AND tweet_id IS NULL");
        }
        if (replyTweetId != null) {
            sql.append(" AND reply_tweet_id = ?");
        } else {
            sql.append(" AND reply_tweet_id IS NULL");
        }
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql.toString())) {
            preparedStatement.setString(1, recipientId);
            preparedStatement.setString(2, actorId);
            preparedStatement.setString(3, type.name());

            int paramIndex = 4;
            if (tweetId != null) {
                preparedStatement.setLong(paramIndex++, tweetId);
            }
            if (replyTweetId != null) {
                preparedStatement.setLong(paramIndex++, replyTweetId);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int getUnreadNotificationCount(String recipientId) {
        String sql = "SELECT COUNT(*) AS count FROM notifications WHERE recipient_id = ? AND is_read = false";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, recipientId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0;
    }
}
