package org.example.repository;

import org.example.model.User;

public interface UserRepository {
    public void createUser(User user);
    public User getUserById(String userId);
    public User getUserByUsername(String username);
}
