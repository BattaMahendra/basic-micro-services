package com.mahi.order.Feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLoggingConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        // Levels: NONE, BASIC, HEADERS, FULL
        return Logger.Level.BASIC;
    }
}
