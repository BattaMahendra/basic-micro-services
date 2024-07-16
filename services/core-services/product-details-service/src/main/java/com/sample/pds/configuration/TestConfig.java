package com.sample.pds.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
//this annotation loads the properties with prefix "app" into the fields of this class
//for this to work you need to enable @EnableConfigurationProperties(TestConfig.class) in the configuration class
@ConfigurationProperties(prefix = "app")

/*
* For configurationProperties to work we need getters and setters */
@Getter
@Setter
public class TestConfig {

    private String name;
    private String myName;
    private int number;
    private boolean state;

    /*
    *
    * After the TestConfig bean is constructed I am verifying whether values got injected into fields*/
    @PostConstruct
    public void intit(){
        System.out.println("injected props : "+name +"\n"+myName+"\n"+number);
    }
}
