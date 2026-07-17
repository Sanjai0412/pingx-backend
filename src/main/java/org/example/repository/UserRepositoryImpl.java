package org.example.repository;

import org.example.config.DatabaseConnection;
import org.example.dto.UserProfileResponse;
import org.example.dto.UserResponse;
import org.example.model.User;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.time.ZoneId;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO users (id, username, display_name, bio, profile_img_url, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getDisplayName());
            preparedStatement.setString(4, user.getBio());
            preparedStatement.setString(5, user.getProfileImgUrl());

            java.time.ZonedDateTime createdAt = user.getCreatedAt();
            if (createdAt == null) {
                createdAt = java.time.ZonedDateTime.now(java.time.ZoneId.of("UTC"));
            }
            preparedStatement.setTimestamp(6, Timestamp.from(createdAt.toInstant()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public UserProfileResponse getUserByUsername(String username) {
        String sql = """
                SELECT u.id, u.username, u.display_name, u.bio, u.profile_img_url, u.created_at,
                (SELECT COUNT(*) FROM followers f WHERE f.followed_id = u.id) AS followers_count,
                (SELECT COUNT(*) FROM followers f WHERE f.follower_id = u.id) AS following_count,
                (SELECT COUNT(*) FROM tweets t WHERE t.user_id = u.id) AS tweets_count
                FROM users u
                WHERE u.username = ?;
                """;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserProfileResponse(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("display_name"),
                        resultSet.getString("bio"),
                        resultSet.getString("profile_img_url"),
                        resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC")),
                        resultSet.getInt("followers_count"),
                        resultSet.getInt("following_count"),
                        resultSet.getInt("tweets_count"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public UserResponse getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new UserResponse(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("display_name"),
                        resultSet.getString("profile_img_url"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> searchUsers(String query, String excludeUserId) {
        List<User> users = new ArrayList<>();
        String sql;
        boolean hasQuery = query != null && !query.trim().isEmpty();

        if (hasQuery) {
            sql = "SELECT * FROM users WHERE (LOWER(username) LIKE ? OR LOWER(display_name) LIKE ?) AND id != ? LIMIT 20";
        } else {
            sql = "SELECT * FROM users WHERE id != ? LIMIT 10";
        }

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            if (hasQuery) {
                String searchPattern = "%" + query.trim().toLowerCase() + "%";
                preparedStatement.setString(1, searchPattern);
                preparedStatement.setString(2, searchPattern);
                preparedStatement.setString(3, excludeUserId);
            } else {
                preparedStatement.setString(1, excludeUserId);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(new User(
                            resultSet.getString("id"),
                            resultSet.getString("username"),
                            resultSet.getString("display_name"),
                            resultSet.getString("bio"),
                            resultSet.getString("profile_img_url"),
                            resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC"))));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, display_name = ?, bio = ?, profile_img_url = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getDisplayName());
            preparedStatement.setString(3, user.getBio());
            preparedStatement.setString(4, user.getProfileImgUrl());
            preparedStatement.setString(5, user.getUserId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User fetchUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getString("id"),
                            resultSet.getString("username"),
                            resultSet.getString("display_name"),
                            resultSet.getString("bio"),
                            resultSet.getString("profile_img_url"),
                            resultSet.getTimestamp("created_at").toInstant().atZone(java.time.ZoneId.of("UTC")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
