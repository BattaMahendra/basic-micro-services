package com.mahi.user.service;


import com.mahi.user.entity.User;
import com.mahi.user.exceptions.ResourceNotFoundException;
import com.mahi.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    /*
    * Setter dependency injection*/
    @Autowired
    public void setUserRepositoryDependency(UserRepository userRepository){
        this.userRepository=userRepository;
    }



    @Override
    @Transactional // Ensures the entire method runs in a single transaction.
    // when we update something then the below method is executed and also the new result is stored in cache manager "users"
    @CachePut(value = "users", key = "#result.id")
    public User createUser(User user) {
        log.info("Creating a new user with email: {}", user.getEmail());
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    @Transactional
    // when we update something then the below method is executed and also the new result is stored in cache manager "users"
    @CachePut(value = "users", key = "#id")
    public User updateUser(Long id, User userDetails) {
        log.info("Attempting to update user with ID: {}", id);
        // Key Change: Fetch the user first or throw a clear exception.
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setName(userDetails.getName());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setAge(userDetails.getAge());

        User updatedUser = userRepository.save(existingUser);
        log.info("Successfully updated user with ID: {}", id);
        return updatedUser;
    }

    @Override
    @Cacheable(value = "users")      // Caches the List of users into "users" cache manager
    @Transactional(readOnly = true) // Optimization for read-only operations.
    public List<User> getAllUsers() {
        log.info("Fetching all users.");
        return userRepository.findAll();
    }

    @Override
    @Cacheable(value = "usersById", key = "#id", unless = "#result == null")  // Caches the User by id and doesn't cache if result is null
    @Transactional(readOnly = true)
    public User getUserById(Long id) { // Corrected method name
        log.info("Fetching user with ID: {}", id);
        // Key Change: Throws our custom exception instead of a generic one.
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    @CacheEvict(value = "usersById", key = "#id")
    public void deleteUser(Long id) {
        log.warn("Attempting to delete user with ID: {}", id);
        // Key Change: Check for existence before deleting to provide a better error.
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. User not found with id: " + id);
        }
        userRepository.deleteById(id);
        log.info("Successfully deleted user with ID: {}", id);
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
