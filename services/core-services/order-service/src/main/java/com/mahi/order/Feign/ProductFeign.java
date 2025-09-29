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
