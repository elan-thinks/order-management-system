package com.example.ordermanagement.presentation.controller;

import com.example.ordermanagement.application.command.*;
import com.example.ordermanagement.application.query.GetOrdersQuery;
import com.example.ordermanagement.application.query.OrderQueryHandler;
import com.example.ordermanagement.domain.repository.ProductRepository;
import com.example.ordermanagement.domain.repository.CustomerRepository; // Added
import com.example.ordermanagement.domain.model.Customer; // Added
import com.example.ordermanagement.domain.value.Address;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class OrderViewController {

    private final OrderQueryHandler queryHandler;
    private final PlaceOrderHandler placeHandler;
    private final DeleteOrderHandler deleteHandler;
    private final UpdateStatusHandler updateHandler;
    private final CreateProductHandler createProductHandler;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository; // 1. Add CustomerRepository

    public OrderViewController(OrderQueryHandler q, PlaceOrderHandler p,
                               DeleteOrderHandler d, UpdateStatusHandler u,
                               CreateProductHandler createProductHandler,
                               ProductRepository productRepository,
                               CustomerRepository customerRepository) { // 2. Add to Constructor
        this.queryHandler = q;
        this.placeHandler = p;
        this.deleteHandler = d;
        this.updateHandler = u;
        this.createProductHandler = createProductHandler;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("stats", queryHandler.getStats());
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("stats", queryHandler.getStats());
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }

    @GetMapping("/orders")
    public String ordersList(Model model) {
        model.addAttribute("orders", queryHandler.handle(new GetOrdersQuery()));
        model.addAttribute("stats", queryHandler.getStats());
        model.addAttribute("activePage", "orders");
        return "orders";
    }

    @GetMapping("/products")
    public String productsList(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("activePage", "products");
        return "products";
    }

    // 3. ADD THIS: Profile mapping to show your Eden Admasu data
    @GetMapping("/profile")
    public String showProfile(Model model) {
        // Fetch the static user 'user_001' from the database
        Customer user = customerRepository.findByAuthId("user_001")
                .orElseThrow(() -> new RuntimeException("Static user not found in DB!"));

        model.addAttribute("user", user);
        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute CreateProductCommand command) {
        createProductHandler.handle(command);
        return "redirect:/products";
    }

    @PostMapping("/orders")
    public String addOrder(@RequestParam String product,
                           @RequestParam int quantity,
                           @RequestParam double price) {
        Address defaultAddress = new Address();
        placeHandler.handle(new PlaceOrderCommand(
                "user_001",
                defaultAddress,
                List.of(new PlaceOrderCommand.ItemData(product, quantity, price))
        ));
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/done")
    public String markAsDone(@PathVariable String id) { // CHANGED: Long to String
        updateHandler.handle(new UpdateStatusCommand(id, "DELIVERED"));
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) { // CHANGED: Long to String
        deleteHandler.handle(new DeleteOrderCommand(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/orders/{id}/ship")
    public String shipOrder(@PathVariable String id) { // Change to String!
        UpdateStatusCommand command = new UpdateStatusCommand(id, "SHIPPED");
        updateHandler.handle(command);
        return "redirect:/";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable String id) { // Change Long to String here
        UpdateStatusCommand command = new UpdateStatusCommand(id, "CANCELLED");

        updateHandler.handle(command);
        return "redirect:/";
    }
}