package org.example.service;

import java.util.List;

import org.example.dto.UserProfileResponse;
import org.example.model.User;

public interface UserService {
    public void registerUser(User user);

    public User getUserProfileById(String userId);

    public UserProfileResponse getUserProfileByUsername(String username);

    public List<User> searchUsers(String query, String excludeUserId);
}
