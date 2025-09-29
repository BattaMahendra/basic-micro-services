package com.mahi.order.entity;


import jakarta.persistence.Embeddable;

@Embeddable
public class Product {

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    private Long id;

    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;

    // Getters and Setters
}
