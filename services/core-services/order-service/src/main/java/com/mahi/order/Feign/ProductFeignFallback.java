package com.mahi.order.Feign;

import com.mahi.order.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductFeignFallback implements ProductFeign {

    @Override
    public Product getProductById(Long id) {
        // Return a default product or throw a custom exception
        Product defaultProduct = new Product();
        defaultProduct.setId(id);
        defaultProduct.setPrice(0.0);
        return defaultProduct;
    }
}
