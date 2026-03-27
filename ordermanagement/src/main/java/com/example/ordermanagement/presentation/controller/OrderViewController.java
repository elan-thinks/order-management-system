package com.example.ordermanagement.presentation.controller;

import com.example.ordermanagement.application.command.*;
import com.example.ordermanagement.application.query.*;
import com.example.ordermanagement.domain.model.Customer;
import com.example.ordermanagement.domain.model.Product;
import com.example.ordermanagement.domain.repository.CustomerRepository;
import com.example.ordermanagement.domain.repository.ProductRepository;
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
    private final CustomerRepository customerRepository;
    private final GetOrdersHandler getOrdersHandler;

    public OrderViewController(OrderQueryHandler q, PlaceOrderHandler p,
                               DeleteOrderHandler d, UpdateStatusHandler u,
                               CreateProductHandler createProductHandler,
                               ProductRepository productRepository,
                               CustomerRepository customerRepository,
                               GetOrdersHandler getOrdersHandler) {
        this.queryHandler = q;
        this.placeHandler = p;
        this.deleteHandler = d;
        this.updateHandler = u;
        this.createProductHandler = createProductHandler;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.getOrdersHandler = getOrdersHandler;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("stats", queryHandler.getStats());
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        return "dashboard";
    }

    @GetMapping("/orders")
    public String listOrders(@RequestParam(required = false) String query, Model model) {
        // This is now the ONLY method for /orders
        GetOrdersQuery getOrdersQuery = new GetOrdersQuery(query);
        List<OrderResponse> orders = getOrdersHandler.handle(getOrdersQuery);

        model.addAttribute("orders", orders);
        model.addAttribute("activePage", "orders");
        return "orders";
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String query, Model model) {
        List<Product> products;
        if (query != null && !query.trim().isEmpty()) {
            products = productRepository.search(query);
        } else {
            products = productRepository.findAll();
        }
        model.addAttribute("products", products);
        model.addAttribute("activePage", "products");
        return "products";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Customer user = customerRepository.findByAuthId("user_001")
                .orElseThrow(() -> new RuntimeException("Static user not found in DB!"));
        model.addAttribute("user", user);
        model.addAttribute("activePage", "profile");
        return "profile";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute CreateProductCommand command, Model model) {
        try {
            createProductHandler.handle(command);
            return "redirect:/products?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("products", productRepository.findAll());
            return "products";
        }
    }

    @PostMapping("/orders")
    public String addOrder(@RequestParam String product,
                           @RequestParam int quantity) {
        Address defaultAddress = new Address("HiLCoE Block B", "Addis Ababa", "Ethiopia");
        placeHandler.handle(new PlaceOrderCommand("user_001", product, quantity, defaultAddress));
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/done")
    public String markAsDone(@PathVariable String id) {
        updateHandler.handle(new UpdateStatusCommand(id, "DELIVERED"));
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/ship")
    public String shipOrder(@PathVariable String id) {
        updateHandler.handle(new UpdateStatusCommand(id, "SHIPPED"));
        return "redirect:/orders";
    }

    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable String id) {
        updateHandler.handle(new UpdateStatusCommand(id, "CANCELLED"));
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        deleteHandler.handle(new DeleteOrderCommand(id));
        return ResponseEntity.ok().build();
    }
}