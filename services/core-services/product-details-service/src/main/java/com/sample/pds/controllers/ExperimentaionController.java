package com.sample.pds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exp")
public class ExperimentaionController {

    @Autowired
    String welcomeString;

    @Autowired
    @Qualifier("sendOffBean")
    String sendOffString;

    @GetMapping("/welcome")
    public String experimentWithBeans(){

    return welcomeString +"\n\n"+ sendOffString;

    }


}
