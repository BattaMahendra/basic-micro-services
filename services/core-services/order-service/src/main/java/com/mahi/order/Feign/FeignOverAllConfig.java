package com.mahi.order.Feign;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignOverAllConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        // Levels: NONE, BASIC, HEADERS, FULL
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignClientInterceptor();
    }
}
