package com.mahi.order.model;

import com.mahi.order.entity.Product;
import com.mahi.order.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderDetail {

    private Long id;

    private User user;
    private Product product;// reference to Product

    private Integer quantity;
    private LocalDateTime orderDate;
}
