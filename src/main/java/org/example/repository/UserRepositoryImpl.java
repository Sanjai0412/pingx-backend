package org.example.repository;

import org.example.config.DatabaseConnection;
import org.example.model.User;

import java.sql.*;
import java.time.ZoneId;

public class UserRepositoryImpl implements UserRepository{
    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO users (id, username, display_name, bio, profile_img_url, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){

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
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("display_name"),
                        resultSet.getString("bio"),
                        resultSet.getString("profile_img_url"),
                        resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC"))
                );
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUserById(String userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql)){

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("display_name"),
                        resultSet.getString("bio"),
                        resultSet.getString("profile_img_url"),
                        resultSet.getTimestamp("created_at").toInstant().atZone(ZoneId.of("UTC"))
                );
            }
        }catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
