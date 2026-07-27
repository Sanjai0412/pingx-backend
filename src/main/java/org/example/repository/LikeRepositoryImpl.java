package org.example.repository;

import org.example.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LikeRepositoryImpl implements LikeRepository {
    @Override
    public boolean likeTweet(String userId, Long tweetId) {
        String sql = "INSERT INTO likes (user_id, tweet_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            preparedStatement.setLong(2, tweetId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean unlikeTweet(String userId, Long tweetId) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND tweet_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            preparedStatement.setLong(2, tweetId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean isLiked(String userId, Long tweetId) {
        String sql = "SELECT 1 FROM likes WHERE user_id = ? AND tweet_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            preparedStatement.setLong(2, tweetId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
