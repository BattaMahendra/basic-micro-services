package com.mahi.order.controller;

import com.mahi.order.model.Order;
import com.mahi.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/{userId}/{productId}")
    public ResponseEntity<Order> createOrder(@PathVariable int userId, @PathVariable int productId) {

        Order order = null;
        order = orderService.createOrder(userId, productId);

        if (order != null) return ResponseEntity.ok(order);

        return (ResponseEntity<Order>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);

}

}
