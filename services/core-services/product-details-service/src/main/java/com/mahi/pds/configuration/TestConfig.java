package com.mahi.pds.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.sql.DataSource;

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

    @Autowired
    private DataSource dataSource;

    /*
    *
    * After the TestConfig bean is constructed I am verifying whether values got injected into fields*/
    @PostConstruct
    public void intit(){
        System.out.println("injected props : "+name +"\n"+myName+"\n"+number);
    }

    /*
    * 1. This will be executed  only once, just before Spring removes our bean from the application context.
    * 2. Same like @PostConstruct method , it can't be static.
    * 3. It can have any level of access modifier
    *
    * Generally used to close DB connections before terminating Spring boot application*/
    @PreDestroy
    public void preDestroy() {

        System.out.println("Pre Destroy method working after beans closeup");

        if(dataSource instanceof AutoCloseable){
            try {((AutoCloseable) dataSource).close();} catch (Exception e) {throw new RuntimeException(e);}
        }
    }
}
