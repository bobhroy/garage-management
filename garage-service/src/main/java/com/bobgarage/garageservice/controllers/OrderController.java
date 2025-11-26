package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.CreateOrderRequest;
import com.bobgarage.garageservice.grpc.BillingServiceGrpcClient;
import com.bobgarage.garageservice.services.CartService;
import com.bobgarage.garageservice.services.CustomerService;
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

    private final CustomerService customerService;
    private final CartService cartService;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    @PostMapping("/{customerId}/create")
    @Operation(summary = "Create a new billing account.")
    public ResponseEntity<?> createOrder(
            @Parameter(description = "The ID of the customer.")
            @PathVariable UUID customerId,
            @Valid @RequestBody CreateOrderRequest request)
    {
        var customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Customer not found."));
        }

        var cart = cartService.getCart(request.getCartId());
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart not found."));
        }

        var response = billingServiceGrpcClient.createBillingAccount(customer.getId(), cart.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("orderId", response.getOrderId()));
    }
}
