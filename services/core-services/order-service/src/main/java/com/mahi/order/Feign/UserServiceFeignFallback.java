package com.mahi.order.Feign;

import com.mahi.order.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class UserServiceFeignFallback implements UserServiceFeign {

    @Override
    public User createUser(User user) {
        return new User();
    }

    @Override
    public List<User> getAllUsers() {
        return Collections.emptyList();
    }

    @Override
    public User getUserById(Long id) {
        return new User();
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        return new User();
    }

    @Override
    public void deleteUser(Long id) {
        // Log fallback execution
    }
}
