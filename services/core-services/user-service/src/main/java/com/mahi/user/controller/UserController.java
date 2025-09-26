package com.mahi.user.controller;


import com.mahi.user.entity.User;
import com.mahi.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    // Update user
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setAge(userDetails.getAge());
            return userRepository.save(user);
        }).orElseThrow();
    }

    // Delete user
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PostConstruct
    public void loadUsers() {
        if (userRepository.count() == 0) {
            List<User> users = new ArrayList<>();
            for (int i = 1; i <= 20; i++) {
                users.add(new User(null, "User" + i, "user" + i + "@gmail.com", 20 + i));
            }
            userRepository.saveAll(users);
            System.out.println("✅ 20 sample users loaded into H2 database!");
        }
    }
}
