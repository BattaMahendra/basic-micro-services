package com.mahi.order.controller;

import com.mahi.order.entity.Order;
import com.mahi.order.model.OrderDetail;
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
    public ResponseEntity<OrderDetail> createOrder(@PathVariable int userId, @PathVariable int productId) {

        OrderDetail order = null;
        order = orderService.createOrder(userId, productId);

        if (order != null) return ResponseEntity.ok(order);

        return (ResponseEntity<OrderDetail>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);

}

}
