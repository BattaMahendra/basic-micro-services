package com.mahi.pds;

import com.mahi.pds.configuration.TestConfig;
import com.mahi.pds.configuration.custom_auto_configuration.pricing_strategy.PriceCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.math.BigDecimal;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.sample.pds"})
//this annotation is used to load properties into fields of TestConfig class
@EnableConfigurationProperties(TestConfig.class)

@EnableEurekaClient // required for lower spring boot versions
public class ProductDetailsServiceApplication {



	public static void main(String[] args) {

		ConfigurableApplicationContext context =
		SpringApplication.run(ProductDetailsServiceApplication.class, args);

		//ApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment(); // we can access environment variables through context

		/*
		* Custom auto configuration test*/
		PriceCalculator priceCalculator = context.getBean(PriceCalculator.class);
		ProductDetailsServiceApplication.log.warn("Final price: " + priceCalculator.calculatePrice(new BigDecimal("100")));


	}

}
