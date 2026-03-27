package com.example.ordermanagement.presentation.controller;

import com.example.ordermanagement.application.command.PlaceOrderCommand;
import com.example.ordermanagement.application.command.PlaceOrderHandler;
import com.example.ordermanagement.domain.repository.ProductRepository;
import com.example.ordermanagement.domain.value.Address;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/store")
public class CustomerStoreController {

    private final ProductRepository productRepository;
    private final PlaceOrderHandler placeOrderHandler;

    public CustomerStoreController(ProductRepository p, PlaceOrderHandler h) {
        this.productRepository = p;
        this.placeOrderHandler = h;
    }

    // 1. The "Storefront" view for the Customer
    @GetMapping
    public String browseProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "storefront";
    }

    // 2. The "Buy" action
    @PostMapping("/buy")
    public String buyProduct(@ModelAttribute PlaceOrderCommand command, RedirectAttributes ra) {
        // 1. Create the address object manually
        Address campusAddress = new Address("HiLCoE Block B", "Addis Ababa", "251");

        try {
            // 2. Wrap the form data + our address into a FINAL command
            PlaceOrderCommand finalCommand = new PlaceOrderCommand(
                    command.authUserId(),   // This will now be "user_001"
                    command.productName(),
                    command.qty(),
                    campusAddress           // This fills the 'shippingAddress' slot
            );

            placeOrderHandler.handle(finalCommand);
            ra.addFlashAttribute("success", "Order placed! Collect at Block B.");
            return "redirect:/store";

        } catch (Exception e) {
            // This will now catch "Not enough stock" or other issues gracefully
            ra.addFlashAttribute("error", "Transaction failed: " + e.getMessage());
            return "redirect:/store";
        }
    }
}
