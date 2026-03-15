package com.example.ordermanagement.presentation.controller;

import com.example.ordermanagement.application.command.*;
import com.example.ordermanagement.application.query.OrderQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/") // Root access to match your HTML
public class OrderViewController {

    private final OrderQueryHandler queryHandler;
    private final PlaceOrderHandler placeHandler;
    private final DeleteOrderHandler deleteHandler;
    private final UpdateStatusHandler updateHandler;

    public OrderViewController(OrderQueryHandler q, PlaceOrderHandler p,
                               DeleteOrderHandler d, UpdateStatusHandler u) {
        this.queryHandler = q;
        this.placeHandler = p;
        this.deleteHandler = d;
        this.updateHandler = u;
    }

    // Displays the Dashboard
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("orders", queryHandler.handleAll());
        model.addAttribute("stats", queryHandler.getStats());
        return "index";
    }

    // Handles "Quick Add Order" Form
    @PostMapping("/orders")
    public String addOrder(@RequestParam String product,
                           @RequestParam int quantity,
                           @RequestParam double price) {
        placeHandler.handle(new PlaceOrderCommand(
                List.of(new PlaceOrderCommand.ItemData(product, quantity, price))
        ));
        return "redirect:/";
    }

    // Handles the "Done" button (Updates status to Delivered)
    @PostMapping("/orders/{id}/done")
    public String markAsDone(@PathVariable String id) {
        updateHandler.handle(new UpdateStatusCommand(id, "Delivered"));
        return "redirect:/";
    }

    // Handles the JavaScript "Delete" call
    @DeleteMapping("/orders/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        deleteHandler.handle(new DeleteOrderCommand(id));
        return ResponseEntity.ok().build();
    }
}