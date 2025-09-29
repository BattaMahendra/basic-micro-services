package com.mahi.order.service;

import com.mahi.order.Exception.OrderServiceException;
import com.mahi.order.Feign.ProductFeign;
import com.mahi.order.Repo.OrderRepository;
import com.mahi.order.config.Producer;
import com.mahi.order.entity.Order;
import com.mahi.order.entity.Product;
import com.mahi.order.entity.User;
import com.mahi.order.model.OrderDetail;
import com.mahi.order.util.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Service
public class OrderService {

    private  ApiClient apiClient;
    private OrderRepository repository;
    private Producer producer;

    @Autowired
    private ProductFeign productFeign;

    public  OrderService(ApiClient apiClient, OrderRepository orderRepository, Producer producer){
        this.apiClient = apiClient;
        this.repository = orderRepository;
        this.producer= producer;
    }


    public OrderDetail createOrder(int userId, int productId){

        OrderDetail orderDetail = new OrderDetail();
        Order order = new Order();

        User user = null;
        Product product = null;
        try {
            // order.setId(new Random().nextLong());
             user = apiClient.getUserDetails(userId);
             product = productFeign.getProductById((long) productId);

            System.out.println("Queried results for\n user:\n "+ user+ "\n product: "+product);
        } catch (Exception e) {
            throw new OrderServiceException(e.getMessage(),e.getCause());
        }
        if(user != null && user.getId() == userId && product!=null){
            order.setUserId((long) userId);
            order.setProductId((long) productId);
            order = repository.save(order);

        }
        if(!ObjectUtils.isEmpty(order)){
            order.setOrderDate(LocalDateTime.now());
            producer.sendMessage(order.toString());

            orderDetail.setUser(user);
            orderDetail.setProduct(product);
            orderDetail.setId(order.getId());
            orderDetail.setOrderDate(order.getOrderDate());
        }
        else producer.sendMessage("Order fetching is not successful");

        return orderDetail;
    }


}
