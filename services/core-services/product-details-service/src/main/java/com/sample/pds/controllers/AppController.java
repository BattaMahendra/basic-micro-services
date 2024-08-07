package com.sample.pds.controllers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController
{


    /*
    *
    * You can create beans in @Component classes also
    * but here you will have disadvantage, whenever same bean is called in the same class then
    * instead of returning same bean it will create new bean every time
    *
    * but if you declare class with @Configuration class then this problem will not occur
    * it will always return same instance. Means the default scope of bean will always be singleton
    *
    * */
    @Bean
    public String testComponentClass(){
        System.out.println("We are in controller class");
        return new String("news");
    }

    @Bean
    public int testAboveAddress(){
        System.out.println("controller class bean address "+System.identityHashCode(testComponentClass()));
        System.out.println("controller class bean address "+System.identityHashCode(testComponentClass()));

        return 1;
    }

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome.html";
    }


}
