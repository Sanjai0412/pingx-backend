package org.example.service;

import java.util.List;

import org.example.dto.UserProfileResponse;
import org.example.dto.UserResponse;
import org.example.model.User;

public interface UserService {
    public void registerUser(User user);

    public UserResponse getUserById(String userId);

    public UserProfileResponse getUserProfileByUsername(String username);

    public List<User> searchUsers(String query, String excludeUserId);

    public UserResponse updateUser(User user);
}
