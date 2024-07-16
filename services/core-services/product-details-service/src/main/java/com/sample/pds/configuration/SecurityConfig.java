package com.sample.pds.configuration;


import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public  class SecurityConfig extends WebSecurityConfigurerAdapter {
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()//.authenticated()
                .permitAll()
                .and()
                .oauth2ResourceServer().jwt();
    }
}
