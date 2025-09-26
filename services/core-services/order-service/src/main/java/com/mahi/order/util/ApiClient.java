package com.mahi.order.util;

import com.mahi.order.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient {

    @Autowired
    private RestTemplate restTemplate;

    public User getUserDetails(long id){

        User user = null;
        String uri = "http://localhost:801/users/"+id;
        ResponseEntity<User> entity = restTemplate.getForEntity(uri, User.class);

        if(entity.hasBody()) return  entity.getBody();

        return entity.getBody();
    }
}
