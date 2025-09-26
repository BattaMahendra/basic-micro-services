package com.mahi.order.service;

import com.mahi.order.Repo.OrderRepository;
import com.mahi.order.model.Order;
import com.mahi.order.model.User;
import com.mahi.order.util.ApiClient;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OrderService {

    private  ApiClient apiClient;
    private OrderRepository repository;

    public  OrderService(ApiClient apiClient, OrderRepository orderRepository){
        this.apiClient = apiClient;
        this.repository = orderRepository;
    }


    public Order createOrder(int userId, int productId){

        Order order = new Order();

       // order.setId(new Random().nextLong());
        User user = apiClient.getUserDetails(userId);
        if(user != null && user.getId() == userId){
            order.setUserId((long) userId);
            order = repository.save(order);

        }


        return order;
    }


}
