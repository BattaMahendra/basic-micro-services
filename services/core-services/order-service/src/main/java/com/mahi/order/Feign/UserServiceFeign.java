package com.mahi.order.Feign;


import com.mahi.order.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
@FeignClient(name = "user-service", url = "http://localhost:801/users")
public interface UserServiceFeign {

    @PostMapping
    public User createUser(@RequestBody User user);

    @GetMapping
    public List<User> getAllUsers();

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id);

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails);

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id);
}
