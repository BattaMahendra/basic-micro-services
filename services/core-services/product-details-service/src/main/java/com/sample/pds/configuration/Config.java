package com.sample.pds.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class Config {



    @Bean
    @Primary
    @Profile("dev")
    public java.lang.String getWelcomeString(){
        return "Welcome Bean";
    }

    @Bean
    @Qualifier("sendOffBean")
    @Profile("dev")
    public java.lang.String sendOffString(){
        return "Bye bye Bean";
    }

    @Bean
    @Qualifier("testingBean")
    @Profile("!dev")     //it will be active in all non dev profiles
    public java.lang.String testInProcessString(){
        return "test Bean";
    }
}
