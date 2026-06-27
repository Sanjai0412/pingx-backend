package org.example.service;

import org.example.model.User;

public interface UserService {
    public void registerUser(User user);
    public User getUserProfileById(String userId);
    public User getUserProfileByUsername(String username);
}
