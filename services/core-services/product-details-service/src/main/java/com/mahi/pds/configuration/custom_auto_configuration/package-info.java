package com.mahi.pds.configuration.custom_auto_configuration;

/**
 *
 * 1. We will be learning about custom auto-configuration in spring boot
 *
 * We already know that autoconfiguration is part of spring boot which injects dependencies
 * based on combination of dependency and required property
 *
 * Business strategy :
 * We will be deciding the pricing strategy of product based on application.properties
 * In application.properties we will give commands for different kinds of pricing strategies like B2B,Festival,default
 *
 * 1. We will set up base interface and define various strategies
 * 2. We need bean based on the value passed in application.properties
 * 3. Set up @ConfigurationProperties class to inject props into class fields
 * 4. Set up auto-configuration class with required annotations and required @Conditional annotation checks
 * 5. Finally register the auto configuration in src/main/resources/META-INF/spring.factories
 *
 *
 * Then everything is setup
 *
 * Testing is done in main class
 *
 * */