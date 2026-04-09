package com.mahi.order.Feign;

import com.mahi.order.entity.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/*
* If you are using Eureka server for registry then in the below line you can omit url part.
* As feign automatically configures the url from eureka registry*/
@FeignClient(name = "product-details-service", configuration = FeignOverAllConfig.class, fallback = ProductFeignFallback.class)
@Component
public interface ProductFeign {

    @CircuitBreaker(name = "product-details-service", fallbackMethod = "getProductByIdFallback")
    @Retry(name = "product-details-service")
    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id);

    default Product getProductByIdFallback(Long id, Throwable t) {
        Product defaultProduct = new Product();
        defaultProduct.setId(id);
        defaultProduct.setPrice(0.0);
        return defaultProduct;
    }
}

/**
 * Interview Questions on Feign
 *
 * 1. How to declare feign clients - syntax
 * 2. Why feign over rest template ?
 * 3. How do you handle exceptions in Feign client ?
 * 4. How do you handle connect time-outs and read time-outs in feign client
 * 5. How do you implement custom error handling in Feign?
 * 6. Explain the use of a RequestInterceptor. Can you provide a practical use case?
 * */

/*
*
             Client Request
                  ↓
            API Gateway
                  ↓
            Order Service

            Spring Filter
                  ↓
            Spring MVC Interceptor
                  ↓
            Controller
                  ↓
            Service
                  ↓
            Resilience4j Layer
               (Retry / CircuitBreaker / RateLimiter / Bulkhead)
                  ↓
            Feign Client
                  ↓
            Feign RequestInterceptor #1
            Feign RequestInterceptor #2
            Feign RequestInterceptor #3
                  ↓
            HTTP Request Sent
                  ↓
            Inventory / Payment Service
* */

/*
*           Resilience 4j - circuit breaker mechanism, retry, timelimiter, rate-limiter, bulkhead
*
*
* --> best if used in service layer.
*
*
                  Failure Threshold(50%)
        CLOSED  -----------------------> OPEN
           ^                              |
           |                              |
           | Success                      | Wait Duration
           |                              v
           ----------- HALF OPEN <-------
* */
