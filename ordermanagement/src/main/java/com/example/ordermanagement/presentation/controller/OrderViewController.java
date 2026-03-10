package com.example.ordermanagement.presentation.controller;

import com.example.ordermanagement.application.command.PlaceOrderCommand;
import com.example.ordermanagement.application.command.PlaceOrderHandler;
import com.example.ordermanagement.application.query.OrderQueryHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web") // This prefixes everything below with /web
public class OrderViewController {
    private final OrderQueryHandler queryHandler;
    private final PlaceOrderHandler commandHandler;

    public OrderViewController(OrderQueryHandler q, PlaceOrderHandler c) {
        this.queryHandler = q;
        this.commandHandler = c;
    }

    // URL: localhost:8080/web/dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("orders", queryHandler.handleAll());

        // Fallback: If getStats() is null, provide an empty map so Thymeleaf doesn't crash
        Object stats = queryHandler.getStats();
        if (stats == null) {
            model.addAttribute("stats", java.util.Map.of("totalOrders", 0, "totalRevenue", 0.0));
        } else {
            model.addAttribute("stats", stats);
        }

        return "index";
    }

    // URL: localhost:8080/web/orders
    @PostMapping("/orders")
    public String addOrder(@RequestParam String product,
                           @RequestParam int quantity,
                           @RequestParam double price) {

        // TRAP: Look at your IntelliJ console after you click "Add Order"
        System.out.println("--- FORM SUBMITTED ---");
        System.out.println("Product: " + product);
        System.out.println("Qty: " + quantity);

        commandHandler.handle(new PlaceOrderCommand(product, quantity, price));
        return "redirect:/web/dashboard";
    }
}