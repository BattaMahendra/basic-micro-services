# 🌟 Spring AOP (Aspect-Oriented Programming) 🌟

![AOP Magic](https://media.giphy.com/media/12NUbkX6p4xOO4/giphy.gif)

## 🤔 What is Aspect-Oriented Programming (AOP)?
Imagine you are building a house 🏠. You have core tasks like building walls, laying the roof, and installing windows (this is your **Core Business Logic**). But you also need things like wiring ⚡, plumbing 🚰, and painting 🎨 that go through every room (these are **Cross-Cutting Concerns**).

AOP is a programming paradigm that helps you separate these cross-cutting concerns (like logging, security, transaction management) from your main business logic. Instead of writing logging code in every single method, you write it once in an **Aspect** and let Spring automatically apply it wherever needed! ✨

## 🦸‍♂️ Why do we need it and how does it help us?
- 🧹 **Cleaner Code**: Your main classes stay focused on their primary job without being cluttered by infrastructure code.
- ♻️ **Reduces Code Duplication**: Write once, apply everywhere! No more copy-pasting logging or security checks.
- 🛠️ **Easier Maintenance**: Want to change how logging works? Update it in exactly ONE place.
- 🧩 **Modularity**: Keeps your application highly decoupled and organized.

## 🕵️‍♀️ Where is it internally used in Spring Boot?
Spring Boot is secretly using AOP magic everywhere! 🪄
1. 🏦 **Transaction Management (`@Transactional`)**: Automatically starts, commits, or rolls back database transactions. You don't see the JDBC code because AOP handles it!
2. ⚡ **Caching (`@Cacheable`, `@CachePut`)**: Intercepts method calls to return cached data instead of running heavy DB queries.
3. 🛡️ **Security (`@PreAuthorize`, `@Secured`)**: Checks if the user has permission *before* letting the method execute.
4. 🔁 **Retry Logic (`@Retryable`)**: Automatically retries a failing method.

---

## 📖 Important Terminologies in AOP
Before diving into code, let's learn the AOP vocabulary! 🗣️

| Term | What it means | Analogy 🧠 |
| :--- | :--- | :--- |
| **Aspect** | The module containing the cross-cutting logic (e.g., a `LoggingAspect` class). | The **Security Guard** 👮‍♂️ hired to check IDs. |
| **JoinPoint** | A specific point in the application execution (like a method being called or an exception being thrown). | **Any Door** 🚪 in the building where the guard *could* stand. |
| **Advice** | The actual action taken by the aspect (the code that runs). | The act of **Checking the ID** 🪪. |
| **Pointcut** | An expression that selects exactly *which* JoinPoints to apply the Advice to. | The specific **Main Entrance Door** 🚪 exactly where the guard is stationed. |
| **Target Object** | The actual object whose method is being called. | The **Person** 🧍‍♂️ trying to enter the building. |
| **Weaving** | The process of linking the Aspect with the Target Object to create an advised object. Spring does this at runtime! | The **Manager assigning** 📋 the guard to the door. |

---

## 🎯 Important Annotations in AOP (Explained Simply)

Let's look at how we build this in Spring Boot! 🚀

### 1. `@Aspect` 🏗️
Tells Spring: *"Hey! This class is not a normal service. It contains cross-cutting logic!"*
*Note: You also need `@Component` so Spring manages it as a bean.*

```java
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    // AOP magic lives here! 🧙‍♂️
}
```

### 2. `@Pointcut` 📍
Defines **WHERE** your advice should run. Instead of writing the same package path in every advice, you define it once here.

```java
import org.aspectj.lang.annotation.Pointcut;

// "Listen to EVERY method inside the 'service' package"
@Pointcut("execution(* com.mahi.pds.service.*.*(..))")
public void allServiceMethods() {
    // This empty method acts as a label/name for the pointcut!
}
```

#### 🔀 Combining Multiple Pointcuts
Yes, you can combine multiple pointcuts in your advice using logical operators:
- `&&` (AND)
- `||` (OR)
- `!` (NOT)

This is super helpful when you want to target specific methods or exclude some!

```java
@Pointcut("execution(* com.mahi.pds.service.*.*(..))")
public void allServiceMethods() {}

@Pointcut("execution(* com.mahi.pds.service.*.get*(..))")
public void getterMethods() {}

// 1. Combine Pointcuts directly in another Pointcut
@Pointcut("allServiceMethods() && !getterMethods()")
public void allServiceMethodsExceptGetters() {}

// 2. Combine Pointcuts inline inside an Advice
@Before("allServiceMethods() && getterMethods()")
public void logOnlyGetterMethods() {
    System.out.println("👀 [BEFORE] A getter method was called in the service layer.");
}
```

### 3. `@Before` 🚦
Runs **BEFORE** the target method starts. Great for logging input parameters or checking security!

```java
import org.aspectj.lang.annotation.Before;

@Before("allServiceMethods()")
public void logBefore() {
    System.out.println("⏳ [BEFORE] Method is about to start...");
}
```

### 4. `@After` 🏁
Runs **AFTER** the method finishes, **no matter what** (even if it throws an exception). Like a `finally` block!

```java
import org.aspectj.lang.annotation.After;

@After("allServiceMethods()")
public void logAfter() {
    System.out.println("✅ [AFTER] Method finished (success or failure)!");
}
```

### 5. `@AfterReturning` 🏆
Runs **ONLY IF** the method completes successfully. You can even grab the returned value!

```java
import org.aspectj.lang.annotation.AfterReturning;

@AfterReturning(pointcut = "allServiceMethods()", returning = "result")
public void logSuccess(Object result) {
    System.out.println("🎉 [SUCCESS] Method returned: " + result);
}
```

### 6. `@AfterThrowing` 💥
Runs **ONLY IF** the method throws an exception. Perfect for centralizing error logging!

```java
import org.aspectj.lang.annotation.AfterThrowing;

@AfterThrowing(pointcut = "allServiceMethods()", throwing = "error")
public void logError(Throwable error) {
    System.out.println("🚨 [ERROR] Method crashed! Reason: " + error.getMessage());
}
```

### 7. `@Around` 🔄 (The Boss!)
The most powerful annotation! It wraps the method entirely. You can:
- Run code before AND after.
- Modify the inputs or outputs.
- Catch exceptions.
- **Stop the method from running at all!**

⚠️ *You MUST call `joinPoint.proceed()` to actually run the target method.*

```java
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

@Around("allServiceMethods()")
public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    System.out.println("▶️ [AROUND - START] Started: " + joinPoint.getSignature().getName());
    
    long startTime = System.currentTimeMillis();
    
    // 👉 THIS EXECUTES THE ACTUAL METHOD 👈
    Object result = joinPoint.proceed(); 
    
    long timeTaken = System.currentTimeMillis() - startTime;
    
    System.out.println("⏹️ [AROUND - END] Finished in " + timeTaken + "ms");
    
    return result; // Return the result back to the caller
}
```

![Mind Blown](https://media.giphy.com/media/xT0xeJpnrWC4XWblWQ/giphy.gif)