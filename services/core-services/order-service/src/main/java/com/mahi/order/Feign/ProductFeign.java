package com.mahi.order.Feign;


import com.mahi.order.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-details-service", url = "http://localhost:808/products/")
public interface ProductFeign {

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id);
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
