package com.mahi.pds.controllers;


import com.mahi.pds.utils.MyService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/scope")
public class ScopeTestController {

    @Autowired @Qualifier("singleton")
    private MyService singleton;

    @Autowired @Qualifier("prototype")
    private ObjectFactory<MyService> prototypeFactory;

    @Autowired @Qualifier("request")
    private MyService requestScoped;

    @Autowired @Qualifier("session")
    private MyService sessionScoped;



    @GetMapping("/beans")
    public  Map<String, Integer> getBeanHashCodes() {
        Map<String, Integer> beans = new HashMap<>();
        beans.put("singleton", singleton.hashCode());
        beans.put("prototype", prototypeFactory.getObject().hashCode());
        beans.put("request", requestScoped.hashCode());
        beans.put("session", sessionScoped.hashCode());

        return beans;
    }


    @Autowired
    public Integer getRank;

    @GetMapping("/rank")
    public Integer getRank(){
        return getRank;
    }
}
