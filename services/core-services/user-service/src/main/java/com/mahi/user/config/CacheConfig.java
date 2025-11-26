package com.mahi.user.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .maximumSize(10_000)            // max entries per JVM
               // .expireAfterWrite(Duration.ofMinutes(10)) // TTL
               // .refreshAfterWrite(Duration.ofMinutes(5)) // proactive refresh
                .recordStats();                 // enable stats
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object,Object> caffeine) {
        CaffeineCacheManager cacheManager
                = new CaffeineCacheManager("users", "usersById"); // Here we have two cache storages
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }


}
