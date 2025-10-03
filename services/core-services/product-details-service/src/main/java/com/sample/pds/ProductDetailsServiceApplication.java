package com.sample.pds;

import com.sample.pds.configuration.TestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashSet;

@SpringBootApplication
@ComponentScan(basePackages = {"com.sample.pds"})
//this annotation is used to load properties into fields of TestConfig class
@EnableConfigurationProperties(TestConfig.class)

@EnableEurekaClient // required for lower spring boot versions
public class ProductDetailsServiceApplication {


	public static void main(String[] args) {

		SpringApplication.run(ProductDetailsServiceApplication.class, args);

	}

}
