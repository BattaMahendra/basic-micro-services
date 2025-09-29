package com.mahi.order.service;

import com.mahi.order.Repo.OrderRepository;
import com.mahi.order.config.Producer;
import com.mahi.order.entity.Order;
import com.mahi.order.entity.User;
import com.mahi.order.model.OrderDetail;
import com.mahi.order.util.ApiClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private  ApiClient apiClient;
    private OrderRepository repository;
    private Producer producer;
    public  OrderService(ApiClient apiClient, OrderRepository orderRepository, Producer producer){
        this.apiClient = apiClient;
        this.repository = orderRepository;
        this.producer= producer;
    }


    public OrderDetail createOrder(int userId, int productId){

        OrderDetail orderDetail = new OrderDetail();
        Order order = new Order();

       // order.setId(new Random().nextLong());
        User user = apiClient.getUserDetails(userId);
        if(user != null && user.getId() == userId){
            order.setUserId((long) userId);
            order = repository.save(order);

        }
        if(!ObjectUtils.isEmpty(order)){
            order.setOrderDate(LocalDateTime.now());
            producer.sendMessage(order.toString());

            orderDetail.setUser(user);
            orderDetail.setId(order.getId());
            orderDetail.setOrderDate(order.getOrderDate());
        }
        else producer.sendMessage("Order fetching is not successful");

        return orderDetail;
    }


}
