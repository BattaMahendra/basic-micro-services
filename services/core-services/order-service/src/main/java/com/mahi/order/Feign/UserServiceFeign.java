package com.mahi.order.Feign;


import com.mahi.order.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-service", url = "http://localhost:801/users")
public interface UserServiceFeign {

    @PostMapping
    public User createUser(@RequestBody User user);
}
