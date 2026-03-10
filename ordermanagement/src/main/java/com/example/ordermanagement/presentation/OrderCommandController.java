//package com.example.ordermanagement.presentation;
//
//import com.example.ordermanagement.application.command.PlaceOrderCommand;
//import com.example.ordermanagement.application.command.PlaceOrderHandler;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/orders")
//public class OrderCommandController {
//    private final PlaceOrderHandler handler;
//
//    public OrderCommandController(PlaceOrderHandler handler) {
//        this.handler = handler;
//    }
//
//    @PostMapping
//    public ResponseEntity<String> placeOrder(@RequestBody PlaceOrderCommand command) {
//        handler.handle(command);
//        return ResponseEntity.ok("Order Placed Successfully!");
//    }
//}