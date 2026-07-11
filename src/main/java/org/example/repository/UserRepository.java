package org.example.repository;

import org.example.dto.UserProfileResponse;
import org.example.model.User;
import java.util.List;

public interface UserRepository {
    public void createUser(User user);

    public User getUserById(String userId);

    public UserProfileResponse getUserByUsername(String username);

    public List<User> searchUsers(String query, String excludeUserId);
}
