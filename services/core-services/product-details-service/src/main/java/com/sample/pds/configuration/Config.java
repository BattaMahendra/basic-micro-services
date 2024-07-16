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
        System.out.println("config class bean address "+System.identityHashCode(testingConfigurationClass()));
        System.out.println("config class bean address "+System.identityHashCode(testingConfigurationClass()));


        return "Bye bye Bean";
    }

    @Bean
    @Qualifier("testingBean")
    @Profile("!dev")     //it will be active in all non dev profiles
    public java.lang.String testInProcessString(){

        return "test Bean";
    }


    /*
    *
    * When we create beans in class annotated with @Configuration class then it will always
    * return singleton instance. That is the benefit of creating beans in configuration classes
    * That is because
    * @Configuration classes are enhanced by Spring using CGLIB proxies.
    * This enhancement ensures that each @Bean method is called only once and that the same instance (singleton) is returned every time.
    *
    *
    * you can check this by creating beans in classes annotated with @Component
    * you can check that in controller package where I have coded.
    * */
    @Bean
    public String testingConfigurationClass(){
        System.out.println("We are in configuration class");
        return new String("new");
    }
}
