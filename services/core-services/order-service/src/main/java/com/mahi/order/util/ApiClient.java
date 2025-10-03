package com.mahi.order.util;

import com.mahi.order.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.base-uri}")
    private String baseUri;

    public User getUserDetails(long id){

        User user = null;
        String uri = "http://user-service/users/"+id;
        ResponseEntity<User> entity = restTemplate.getForEntity(uri, User.class);

        System.out.println("User extracted for id : "+id+" is :"+ entity.getBody());

        if(entity.hasBody()) return  entity.getBody();

        return entity.getBody();
    }
}
