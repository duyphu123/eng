//package com.smartviet.base.salary.controllers;
//
//import com.smartviet.base.salary.dto.common.ExecutionResult;
//import com.smartviet.base.salary.services.iservice.OrderService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/order")
//public class OrderController {
//
//    private final OrderService orderService;
//
//    @Autowired
//    public OrderController(OrderService orderService) {
//        this.orderService = orderService;
//    }
//
//    @GetMapping("/v1/test")
//    public ResponseEntity<ExecutionResult<String>> test() {
//        log.info("Test endpoint hit, creating order event");
//        orderService.createOrderEvent();
//        return ResponseEntity.ok(
//                ExecutionResult.<String>builder()
//                        .responseCode("200")
//                        .data("Test successful")
//                        .build()
//        );
//    }
//
//    @GetMapping("/v1/test1")
//    public ResponseEntity<ExecutionResult<String>> test1() {
//        return ResponseEntity.ok(
//                ExecutionResult.<String>builder()
//                        .responseCode("200")
//                        .data("Test1 successful")
//                        .build()
//        );
//    }
//
//}
