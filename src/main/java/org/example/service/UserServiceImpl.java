package org.example.service;

import jakarta.inject.Inject;

import java.util.List;

import org.example.dto.UserProfileResponse;
import org.example.dto.UserResponse;
import org.example.model.User;
import org.example.repository.UserRepository;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // HK2 reads this annotation and passes in the UserRepositoryImpl obj
    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void registerUser(User user) {
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        userRepository.createUser(user);
    }

    @Override
    public UserResponse getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        return userRepository.getUserById(userId);
    }

    @Override
    public UserProfileResponse getUserProfileByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        return userRepository.getUserByUsername(username);
    }

    @Override
    public List<User> searchUsers(String query, String excludeUserId) {
        if (excludeUserId == null || excludeUserId.trim().isEmpty()) {
            throw new IllegalArgumentException("Exclude user ID is required");
        }
        return userRepository.searchUsers(query, excludeUserId);
    }
}
