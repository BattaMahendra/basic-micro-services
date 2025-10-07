package com.mahi.order.controller;

import com.mahi.order.exception.OrderServiceException;
import com.mahi.order.entity.Product;
import com.mahi.order.model.OrderDetail;
import com.mahi.order.service.OrderService;
import com.mahi.order.webClient.ProductServiceWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductServiceWebClient productServiceWebClient;

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<?> createOrder(@PathVariable int userId, @PathVariable int productId) {

        OrderDetail order = null;
        try {
            order = orderService.createOrder(userId, productId);
        } catch (OrderServiceException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        if (order != null && order.getId()!=null) return ResponseEntity.ok(order);

        return (ResponseEntity<OrderDetail>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);

}

@GetMapping("/products/{productId}")
    public void getProductById(@PathVariable Long productId) throws InterruptedException {
        Mono<Product> productMono = productServiceWebClient.getProductById(productId);

        /**
         * If you want synchronous nature you can just use block() method on mono or flux objects
         * for eg: productMono.block() --> makes sure to block the thread until execution is completed
         * So that block() can be used to work just like rest template
         * subscribe() is like a async execution and your thread is not blocked*/

        // we will get results only if we subscribe
    productMono.subscribe(System.out::println); // will print only once execution gets completed

    IntStream.range(0,7).forEach(i -> {
        System.out.println(" MONO WAIT --> We are waiting for mono to complete : "+ i);
    });

    Thread.sleep(1000);

    // learning about flux

    Flux<Product> productFlux = productServiceWebClient.getProducts();
    productFlux.subscribe(System.out::println);

    IntStream.range(0,7).forEach(i -> {
        System.out.println(" FLUX WAIT --> We are waiting for mono to complete : "+ i);
    });


}

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Integer> streamNumbers() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1));
    }


}
