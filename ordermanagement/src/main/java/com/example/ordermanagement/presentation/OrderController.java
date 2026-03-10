package com.example.ordermanagement.presentation;

import ch.qos.logback.core.model.Model;
import com.example.ordermanagement.application.command.*;
import com.example.ordermanagement.application.query.OrderQueryHandler;
import com.example.ordermanagement.application.query.OrderResponse;
import com.example.ordermanagement.application.query.OrderStatsResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
// ... other imports

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private UpdateStatusHandler updateHandler;

    @Autowired
    private DeleteOrderHandler deleteHandler;
    private final PlaceOrderHandler placeOrderHandler;
    private final UpdateQuantityHandler updateQuantityHandler; // Standalone type
    private final OrderQueryHandler queryHandler;

    public OrderController(PlaceOrderHandler p, UpdateQuantityHandler u, OrderQueryHandler q) {
        this.placeOrderHandler = p;
        this.updateQuantityHandler = u;
        this.queryHandler = q;
    }

    @PutMapping("/{id}")
    public void updateQuantity(@PathVariable String id, @RequestParam int qty) {
        // This will now find the .handle() method correctly!
        updateQuantityHandler.handle(new UpdateQuantityCommand(id, qty));
    }


    @PostMapping("/update-status")
    public void updateStatus(@RequestParam String orderId,
                             @RequestParam String newStatus,
                             HttpServletResponse response) throws IOException {
        updateHandler.handle(orderId, newStatus);
        // This tells the browser: "The work is done, now go back to the dashboard."
        response.sendRedirect("/");
    }

    @DeleteMapping("/{id}") // Removed the redundant "/api/orders"
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        deleteHandler.handle(id);
        return ResponseEntity.ok().build();
    }


    @Controller
    public class OrderWebController {

        @GetMapping("/")
//        @GetMapping("/")
        public String index(org.springframework.ui.Model model) {
            // 1. Get the data from your Handler
            List<OrderResponse> orders = queryHandler.handleAll();
            OrderStatsResponse stats = queryHandler.getStats();

            // 2. Pass them to Thymeleaf
            model.addAttribute("orders", orders);
            model.addAttribute("stats", stats); // If this line is missing, the page crashes!

            return "index";
        }
    }
}