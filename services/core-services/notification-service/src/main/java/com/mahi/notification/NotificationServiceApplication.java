package com.mahi.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}

}


/*
*
* This application is not a web server. This doesn't run on Tomcat server
* It doesn't have spring-starter-web dependency
* It only runs on command line and not on any port.
* It receives messages and prints them on console
* */
