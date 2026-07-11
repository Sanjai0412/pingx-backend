package org.example.repository;

import org.example.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FollowRepositoryImpl implements FollowRepository {
    @Override
    public boolean followUser(String followerId, String followingId) {
        String sql = "INSERT INTO followers (follower_id, followed_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, followerId);
            preparedStatement.setString(2, followingId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean unfollowUser(String followerId, String followingId) {
        String sql = "DELETE FROM followers WHERE follower_id = ? AND followed_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, followerId);
            preparedStatement.setString(2, followingId);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean isFollowing(String followerId, String followingId) {
        String sql = "SELECT 1 FROM followers WHERE follower_id = ? AND followed_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, followerId);
            preparedStatement.setString(2, followingId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public long getFollowersCount(String userId) {
        String sql = "SELECT COUNT(*) FROM followers WHERE followed_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0;
    }

    @Override
    public long getFollowingCount(String userId) {
        String sql = "SELECT COUNT(*) FROM followers WHERE follower_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return 0;
    }
}
