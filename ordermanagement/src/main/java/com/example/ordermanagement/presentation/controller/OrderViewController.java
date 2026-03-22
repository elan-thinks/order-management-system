package com.example.ordermanagement.presentation.controller;

import com.example.ordermanagement.application.command.*;
import com.example.ordermanagement.application.query.GetOrdersQuery;
import com.example.ordermanagement.application.query.OrderQueryHandler;
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
    // 1. Add the repository to your fields
    private final ProductRepository productRepository;

    public OrderViewController(OrderQueryHandler q, PlaceOrderHandler p,
                               DeleteOrderHandler d, UpdateStatusHandler u, CreateProductHandler createProductHandler, ProductRepository productRepository) {
        this.queryHandler = q;
        this.placeHandler = p;
        this.deleteHandler = d;
        this.updateHandler = u;
        this.createProductHandler = createProductHandler;
        this.productRepository = productRepository;
    }

    // Displays the Dashboard (Frontend)
//    @GetMapping
//    public String index(Model model) {
//        model.addAttribute("orders", queryHandler.handle(new GetOrdersQuery()));
//
//        // Add this so the top boxes on your dashboard aren't zero!
//        model.addAttribute("stats", queryHandler.getStats());
//
//        return "orders";
//    }

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        model.addAttribute("stats", queryHandler.getStats());
    }

    // 1. Keep this for the "Dashboard" view (the summary boxes)
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("stats", queryHandler.getStats());
        model.addAttribute("activePage", "dashboard");
        return "dashboard"; // Assuming your dashboard is index.html
    }

    // 2. ADD THIS: Specifically map the /orders URL
    @GetMapping("/orders")
    public String ordersList(Model model) {
        // This is what fills the table in orders.html
        model.addAttribute("orders", queryHandler.handle(new GetOrdersQuery()));

        // IMPORTANT: The orders.html also uses ${stats} for the top summary boxes
        model.addAttribute("stats", queryHandler.getStats());

        model.addAttribute("activePage", "orders");
        return "orders";
    }

    // 1. Add this method to handle the /products URL
    @GetMapping("/products")
    public String productsList(Model model) {
        // Fetch the data from MySQL and name it "products" so the HTML can see it
        model.addAttribute("products", productRepository.findAll());

        model.addAttribute("activePage", "products");
        return "products";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute CreateProductCommand command) {
        createProductHandler.handle(command);
        return "redirect:/products"; // Refresh the list to show the new item
    }
    // Handles "Quick Add Order" Form
// Handles "Quick Add Order" Form
    @PostMapping("/orders")
//    @PostMapping("/orders")
    public String addOrder(@RequestParam String product,
                           @RequestParam int quantity,
                           @RequestParam double price) {

        Address defaultAddress = new Address();

        placeHandler.handle(new PlaceOrderCommand(
                "user_001",
                defaultAddress,
                List.of(new PlaceOrderCommand.ItemData(product, quantity, price))
        ));

        // Redirect to /orders so you see the new order in the list immediately
        return "redirect:/orders";
    }

    // Handles the "Done" button
    @PostMapping("/orders/{id}/done")
    public String markAsDone(@PathVariable String id) {
        // Status matches our OrderStatus enum string
        updateHandler.handle(new UpdateStatusCommand(id, "DELIVERED"));
        return "redirect:/orders";
    }

    @DeleteMapping("/orders/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        deleteHandler.handle(new DeleteOrderCommand(id));
        return ResponseEntity.ok().build();
    }
    // Updates Order Status to SHIPPED
    @PostMapping("/orders/{id}/ship")
    public String shipOrder(@PathVariable String id) {
        updateHandler.handle(new UpdateStatusCommand(id, "SHIPPED"));
        return "redirect:/orders";
    }

    // Updates Order Status to CANCELLED (Inventory should be "returned" logic here later)
    @PostMapping("/orders/{id}/cancel")
    public String cancelOrder(@PathVariable String id) {
        updateHandler.handle(new UpdateStatusCommand(id, "CANCELLED"));
        return "redirect:/";
    }
}