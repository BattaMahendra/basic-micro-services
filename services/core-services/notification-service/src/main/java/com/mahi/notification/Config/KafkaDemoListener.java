package com.mahi.notification.Config;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaDemoListener {

    @KafkaListener(topics = "basic_microservices_topic", groupId = "basic_microservices_group")
    public void receiver(String message){
        System.out.println("Received the message : "+message);
    }
}
