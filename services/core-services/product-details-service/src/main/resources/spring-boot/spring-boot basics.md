# Spring Boot Basics

## JEE vs Spring vs Spring Boot

*   **JEE (Java Enterprise Edition)**: A set of specifications and APIs for developing enterprise-level applications in Java. It provides a robust platform for building distributed, multi-tier applications. Implementing a JEE application often involves a lot of configuration and boilerplate code.
*   **Spring Framework**: At its core, Spring framework is really just a dependency injection container, with a couple of convenience layers (think: database access, proxies, aspect-oriented programming, RPC, a web mvc framework) added on top. It helps you build Java application faster and more conveniently. It even helps you to read properties files using annotation `@PropertySource`.
*   **Spring Boot**: Spring Boot is a project that is built on the top of the Spring Framework. It provides an easier and faster way to set up, configure, and run both simple and web-based applications. It is a combination of Spring and embedded servers (tomcat by default) and spring boot removes the need of XML configuration for beans and thus reducing lot of boiler plate code. So, Spring Boot is all about taking the existing Spring framework parts, pre-configuring and packaging them up - with as little development work needed as possible. Example: always booting up an embedded Tomcat so you can immediately see the results of writing your `@RestControllers`.

## How Spring Boot helps us (Advantages)

The main goal of Spring Boot is to reduce development, unit test, and integration test time. It provides a RAD (Rapid Application Development) feature to the Spring framework.
By providing or avoiding the below points, Spring Boot Framework reduces Development time, Developer Effort, and increases productivity.
*   Provides Opinionated Development approach
*   Avoids defining more Annotation Configuration
*   Avoids writing lots of import statements
*   Avoids XML Configuration.

## Disadvantages of Spring Boot

*   Spring Boot can use dependencies that are not going to be used in the application.
*   These dependencies increase the size of the application.

## IOC Principle (Inversion of Control)

It is a design principle through which we can create and maintain the objects in a container away from the original code implementation. Which means we are taking control of flow of program (creating and maintaining beans) from the original code and doing it with a spring container.

**How it can be achieved:**
IOC can be achieved through:
1.  Dependency Injection
2.  Factory design pattern
3.  Service locator design pattern

## Dependency Injection

It is a design pattern with which we achieve IOC.
Dependency injection generally means passing a dependent object as a parameter to a method, rather than having the method create the dependent object.
What it means in practice is that the method does not have a direct dependency on a particular implementation; any implementation that meets the requirements can be passed as a parameter.
With this implementation of objects defines their dependencies. And spring makes it available. This leads to loosely coupled application development.

**Quick Example:** EMPLOYEE OBJECT WHEN CREATED, IT WILL AUTOMATICALLY CREATE ADDRESS OBJECT (if address is defined as a dependency by Employee object).

## Auto-configuration

This is the magical feature which makes spring boot apart from spring. This feature, enabled by `@EnableAutoConfiguration` (which is part of `@SpringBootApplication`), configures all the required configuration to run your spring boot application. Without this feature (i.e., usually in plain Spring applications), you have to define configurations for everything (e.g. for `DispatcherServlet` as well). This can be avoided with spring boot auto configuration.

**How it works exactly (e.g., Database Connection):**
If you add Spring data jpa dependency and my sql dependency and required DB props in `application.properties` then automatically connection is established. If you change the DB and the configuration then the connection is automatically shifted to another DB which you have shifted.

1.  Spring boot auto configuration automatically checks for the `DataSource.class` (from Spring-Data-Jpa dependency). If true:
2.  It automatically checks for the `MySqlConnector.class` (from my-sql dependency). If true:
3.  It automatically checks for required configs props in `application.properties`. If true:
4.  Spring boot auto configuration automatically connects app to DB.

All this work is automatically done by spring boot autoconfiguration so that you don't need to do all this configuration. All this set of things to check are kept in `spring-boot-starter-auto-configuration` jar and under that `META-INF` folder and in that `spring.factories`.

**Code Configurations and `@Conditional` annotations:**

All this checking is achieved by using `@Conditional` group annotations which check for the class or props or anything we specify. Auto-configuration classes are regular `@Configuration` classes, heavily guarded by these annotations.

```java
@Configuration
@ConditionalOnClass(DataSource.class)
public class MyDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        // Create and return a default DataSource
        // This only happens if DataSource class is on classpath AND
        // no user-defined DataSource bean already exists
        return new HikariDataSource();
    }
}
```

*   `@ConditionalOnClass`: The configuration will only be applied if the specified class is present on the classpath. (e.g., Only configure a database if a DB driver class is present).
*   `@ConditionalOnMissingBean`: The configuration will only be applied if a bean of the specified type is *not* already defined. This allows developers to easily override auto-configuration by providing their own `@Bean`.
*   `@ConditionalOnProperty`: The configuration will only be applied if a specific Spring environment property has a certain value (or just exists).

## `@SpringBootApplication` annotation

`@SpringBootApplication` is a convenience annotation that combines three commonly used Spring annotations:

1.  `@Configuration`: Tags the class as a source of bean definitions for the application context.
2.  `@EnableAutoConfiguration`: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings (the auto-configuration magic).
3.  `@ComponentScan`: Tells Spring to look for other components, configurations, and services in the specified package (and its sub-packages).

**Why they are needed:** They provide a single point to bootstrap a Spring Boot application, enabling component scanning for your custom code and turning on the auto-configuration engine.

**Excluding unwanted dependent config classes from auto-configuration:**
If you don't want a specific auto-configuration to apply (for example, you want to fully manually configure your database and don't want Spring Boot's `DataSourceAutoConfiguration` to run), you can exclude it using the `exclude` attribute on `@SpringBootApplication`:

```java
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MySpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }
}
```

## Spring IOC Container

The Spring IOC container is the core of the Spring Framework. It creates objects, wires them together, configures them, and manages their complete life cycle from creation till destruction. The container gets its instructions on what objects to instantiate, configure, and assemble by reading configuration metadata provided (XML, Java annotations, or Java code).

**How it is implemented:**
The Spring container is primarily implemented by the `ApplicationContext` interface (and its basic counterpart, `BeanFactory`). The container reads the configuration, looks for beans (like `@Component`, `@Service`, `@Bean`), creates instances of those classes, resolves their dependencies (injects them), and keeps them ready for the application to use.

## ApplicationContext and BeanFactory

These are the two main interfaces that represent the Spring IoC container.

### `BeanFactory`
*   It is the basic, simplest form of the container.
*   Provides basic IoC and Dependency Injection features.
*   **Lazy Loading**: By default, it loads beans lazily (it only instantiates a bean when you explicitly ask for it).
*   Useful in highly memory-constrained environments, but rarely used directly in modern Spring applications.

### `ApplicationContext`
*   It extends `BeanFactory` and adds more enterprise-specific functionality.
*   **Eager Loading**: By default, it pre-instantiates all singleton beans when the application starts up. This helps catch configuration errors early.
*   Provides additional features like:
    *   Internationalization (MessageSource)
    *   Event publishing (ApplicationEventPublisher)
    *   Resource loading
    *   Integration with Spring's AOP

**Types of `ApplicationContext`:**
1.  **`AnnotationConfigApplicationContext`**: Used when you are configuring Spring using Java annotations (like `@Configuration`, `@ComponentScan`). This is the most common in modern Spring/Spring Boot apps.
2.  **`ClassPathXmlApplicationContext`**: Loads context definition from an XML file located in the classpath.
3.  **`FileSystemXmlApplicationContext`**: Loads context definition from an XML file in the file system.
4.  **`AnnotationConfigWebApplicationContext` / `XmlWebApplicationContext`**: Specific contexts used for web applications.

**Difference between `BeanFactory` and `ApplicationContext`:**
The main difference is that `BeanFactory` is basic and lazy-loads beans, whereas `ApplicationContext` is advanced, feature-rich, and eagerly loads singleton beans at startup. `ApplicationContext` is the standard and recommended container for almost all Spring applications.

---
**References:**
*   [Spring Boot Auto-configuration Guide](https://www.marcobehler.com/guides/spring-boot-autoconfiguration)
*   [Scaler Topics: Spring Boot Auto-configuration](https://www.scaler.com/topics/spring-boot/spring-boot-auto-configuration/)