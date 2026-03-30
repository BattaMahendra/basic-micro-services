package com.mahi.pds.models;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Product Response DTO - represents the API response for a product
 * Follows API contract and includes metadata
 */
@Data
public class ProductResponse {

    private Long id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse() {}

    public ProductResponse(Long id, String title, Double price, String description,
                          String category, String image) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}

