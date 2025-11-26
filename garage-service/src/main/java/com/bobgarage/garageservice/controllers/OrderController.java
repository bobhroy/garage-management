package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.CreateOrderRequest;
import com.bobgarage.garageservice.repositories.CartRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
public class OrderController {

    private CartRepository cartRepository;

    @PostMapping("/{customerId}/create")
    @Operation(summary = "Create a new billing account.")
    public ResponseEntity<?> createOrder(
            @Parameter(description = "The ID of the customer.")
            @PathVariable UUID customerId,
            @Valid @RequestBody CreateOrderRequest request)
    {
        System.out.println("CustomerId: " + customerId);
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart not found."));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("orderId", UUID.randomUUID()));
    }
}
