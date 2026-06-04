//package com.smartviet.base.salary.services;
//
//import com.smartviet.base.salary.services.iservice.OrderService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class OrderServiceImpl implements OrderService {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private static final String TOPIC_NAME = "order-created";
//
//    @Autowired
//    public OrderServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    @Override
//    public void createOrderEvent() {
//        // create and save to database logic here
//        log.info("Creating order...");
//        // the end of create and save to database logic
//        log.info("Order created successfully, sending event to Kafka topic: {}", TOPIC_NAME);
//        String message = "Order created: 12345";
//        kafkaTemplate.send(TOPIC_NAME, message);
//    }
//
//}
