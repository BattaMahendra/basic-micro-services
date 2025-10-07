package com.mahi.user.service;


import com.mahi.user.entity.User;
import java.util.List;

// Key Change: An interface defines the service contract, promoting loose coupling.
public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    List<User> getAllUsers();
    User getUserById(Long id); // Corrected method name to match controller
    void deleteUser(Long id);
}