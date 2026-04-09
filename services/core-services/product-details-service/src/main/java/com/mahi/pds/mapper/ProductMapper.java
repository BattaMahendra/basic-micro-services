package com.mahi.pds.mapper;

import com.mahi.pds.entity.Product;
import com.mahi.pds.models.ProductRequest;
import com.mahi.pds.models.ProductResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ProductMapper - Converts between Product Entity, Request DTOs and Response DTOs
 * Implements the mapping pattern for clean separation of concerns
 */
@Component
public class ProductMapper {

    /**
     * Convert ProductRequest to Product Entity
     * Validates and maps all required fields
     *
     * @param productRequest the incoming request DTO
     * @return Product entity ready to be persisted
     * @throws IllegalArgumentException if request is null or invalid
     */
    public Product toEntity(ProductRequest productRequest) {
        Objects.requireNonNull(productRequest, "ProductRequest must not be null");

        // Validate critical fields
        if (productRequest.getTitle() == null || productRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Product title is required and cannot be empty");
        }

        if (productRequest.getPrice() == null || productRequest.getPrice() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }

        if (productRequest.getCategory() == null || productRequest.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Product category is required and cannot be empty");
        }

        Product product = new Product();
        product.setTitle(productRequest.getTitle().trim());
        product.setPrice(productRequest.getPrice());
        product.setDescription(productRequest.getDescription() != null ?
            productRequest.getDescription().trim() : "");
        product.setCategory(productRequest.getCategory().trim());
        product.setImage(productRequest.getImage() != null ?
            productRequest.getImage().trim() : "");

        return product;
    }

    /**
     * Convert Product Entity to ProductResponse DTO
     *
     * @param product the product entity
     * @return product response DTO ready to be sent to client
     */
    public ProductResponse toResponse(Product product) {
        Objects.requireNonNull(product, "Product entity must not be null");

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setCategory(product.getCategory());
        response.setImage(product.getImage());
        response.setCreatedAt(LocalDateTime.now());
        response.setUpdatedAt(LocalDateTime.now());

        return response;
    }
}

