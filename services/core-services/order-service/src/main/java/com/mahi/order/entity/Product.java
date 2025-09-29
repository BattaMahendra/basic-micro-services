package com.mahi.order.entity;

import lombok.*;





@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {


    private Long id;

    private String title;
    private Double price;

    @Override
    public String toString() {
        return "\nProduct{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    private String description;
    private String category;
    private String image;

    // Getters and Setters
}
