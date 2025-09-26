package com.sample.pds.configuration;


import com.sample.pds.utils.MyService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;

@Configuration
public class BeanConfig {



    @Bean
    @Qualifier("singleton")
    public com.sample.pds.utils.MyService singletonBean() {
        return new MyService("SingletonBean");
    }

    @Bean
    @Scope("prototype")
    @Qualifier("prototype")
    public MyService protoTypeBean() {
        return new MyService("PrototypeBean");
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Qualifier("request")
    public MyService perRequestBean() {
        return new MyService("RequestBean");
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Qualifier("session")
    public MyService perSessionBean() {
        return new MyService("SessionBean");
    }


    @Bean
    @Order(1) // @Order decides the sequence when multiple beans are injected as a collection. lowest number is treated as first.
    @Primary // Purpose: If multiple beans of the same type exist, this marks one as the default candidate for autowiring.
    @Lazy   // Purpose: By default, Spring creates beans at startup (eager init). With @Lazy, the bean is created only when first requested.
    public Integer getRank(){
        System.out.println("Getting Rank 1");
        return 1;
    }
    @Bean
    @Order(2)
    @Qualifier("2") //Purpose: Helps disambiguate between beans of the same type by giving them a name/qualifier.
    public Integer getRank2(){
        System.out.println("Getting Rank 2");
        return 2;
    }
    @Bean
    @Order(3)
    public Integer getRank3(){
        System.out.println("Getting Rank 3");
        return 3;
    }
}
