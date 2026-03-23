package com.example.ordermanagement.presentation.controller;

import com.example.ordermanagement.application.command.PlaceOrderCommand;
import com.example.ordermanagement.application.command.PlaceOrderHandler;
import com.example.ordermanagement.domain.repository.ProductRepository;
import com.example.ordermanagement.domain.value.Address;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String buyProduct(@RequestParam String productName, @RequestParam int qty) {
        Address shipping = new Address("Addis Ababa", "HiLCoE Campus", "1000");

        placeOrderHandler.handle(new PlaceOrderCommand(
                "user_001",
                shipping,
                // Ensure this sends the NAME, not the SKU
                List.of(new PlaceOrderCommand.ItemData(productName, qty, 0.0))
        ));

        return "redirect:/store?success=true";
    }
}
