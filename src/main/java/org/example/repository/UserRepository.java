package org.example.repository;

import org.example.dto.UserProfileResponse;
import org.example.dto.UserResponse;
import org.example.model.User;
import java.util.List;

public interface UserRepository {
    public void createUser(User user);

    public UserResponse getUserById(String userId);

    public UserProfileResponse getUserByUsername(String username);

    public List<User> searchUsers(String query, String excludeUserId);

    public List<User> getSuggestedUsers(String excludeUserId, int limit);

    public void updateUser(User user);

    public User fetchUserById(String userId);
}
