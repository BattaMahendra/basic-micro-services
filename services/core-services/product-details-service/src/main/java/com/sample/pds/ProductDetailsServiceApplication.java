package com.sample.pds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashSet;

@SpringBootApplication

@ComponentScan(basePackages = {"com.sample.pds"})
public class ProductDetailsServiceApplication {


	public static void main(String[] args) {

		SpringApplication.run(ProductDetailsServiceApplication.class, args);

	}

}
