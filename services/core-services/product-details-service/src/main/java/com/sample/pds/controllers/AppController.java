package com.sample.pds.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppController
{

    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome";
    }

}
