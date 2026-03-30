package com.mahi.order.Feign;


import com.mahi.order.entity.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Collections;

@Component
@FeignClient(name = "user-service", url = "http://localhost:801/users", configuration = FeignOverAllConfig.class, fallback = UserServiceFeignFallback.class)
public interface UserServiceFeign {

    @PostMapping
    @CircuitBreaker(name = "user-service", fallbackMethod = "createUserFallback")
    @Retry(name = "user-service")
    public User createUser(@RequestBody User user);

    @GetMapping
    @CircuitBreaker(name = "user-service", fallbackMethod = "getAllUsersFallback")
    @Retry(name = "user-service")
    public List<User> getAllUsers();

    @GetMapping("/{id}")
    @CircuitBreaker(name = "user-service", fallbackMethod = "getUserByIdFallback")
    @Retry(name = "user-service")
    public User getUserById(@PathVariable Long id);

    @PutMapping("/{id}")
    @CircuitBreaker(name = "user-service", fallbackMethod = "updateUserFallback")
    @Retry(name = "user-service")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails);

    @DeleteMapping("/{id}")
    @CircuitBreaker(name = "user-service", fallbackMethod = "deleteUserFallback")
    @Retry(name = "user-service")
    public void deleteUser(@PathVariable Long id);
    
    // Fallback methods
    default User createUserFallback(User user, Throwable t) {
        return new User(); 
    }

    default List<User> getAllUsersFallback(Throwable t) {
        return Collections.emptyList();
    }

    default User getUserByIdFallback(Long id, Throwable t) {
         return new User(); 
    }

    default User updateUserFallback(Long id, User userDetails, Throwable t) {
         return new User(); 
    }

    default void deleteUserFallback(Long id, Throwable t) {
        // Log error
    }
}
