package com.mahi.order.util;

import com.mahi.order.entity.User;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "user-service", fallbackMethod = "getDefaultUserDetails")
    public User getUserDetails(long id){

        User user = null;
        String uri = baseUri+id;
        ResponseEntity<User> entity = restTemplate.getForEntity(uri, User.class);

        System.out.println("User extracted for id : "+id+" is :"+ entity.getBody());

        if(entity.hasBody()) return  entity.getBody();

        return entity.getBody();
    }

    /**
     * This is the fallback method.
     * Its signature matches the primary method, plus the Throwable.
     * It is made public so Spring's AOP proxy can access it.
     */
    public User getDefaultUserDetails(long id, Throwable t){
        System.out.println("User-service couldn't be contacted, fall back method executed");
        // Return a default, cached, or simplified object
        return new User(0L,"dummy","dummy",0);
    }
}
