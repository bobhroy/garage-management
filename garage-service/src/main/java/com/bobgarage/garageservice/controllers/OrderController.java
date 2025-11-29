package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.CreateOrderRequest;
import com.bobgarage.garageservice.dtos.OrderDto;
import com.bobgarage.garageservice.dtos.PayOrderRequest;
import com.bobgarage.garageservice.exceptions.CartNotFoundException;
import com.bobgarage.garageservice.exceptions.CustomerNotFoundException;
import com.bobgarage.garageservice.exceptions.EmptyCartDetectedException;
import com.bobgarage.garageservice.exceptions.OrderNotFoundException;
import com.bobgarage.garageservice.services.OrderService;
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

    private final OrderService orderService;

    @PostMapping("/{customerId}/create")
    @Operation(summary = "Create a new billing account.")
    public ResponseEntity<Map<String, String>> createOrder(
            @Parameter(description = "The ID of the customer.")
            @PathVariable UUID customerId,
            @Valid @RequestBody CreateOrderRequest request)
    {
        var response = orderService.createOrder(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("orderId", response.getOrderId()));
    }

    @PostMapping("/{customerId}/process-payment")
    @Operation(summary = "Create a new billing account.")
    public ResponseEntity<String> payOrder(
            @Parameter(description = "The ID of the customer.")
            @PathVariable UUID customerId,
            @Valid @RequestBody PayOrderRequest request)
    {
        var response = orderService.payOrder(customerId, request);
        return ResponseEntity.ok(response.getStatus());
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Customer not found."));
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Order not found."));
    }

    @ExceptionHandler(EmptyCartDetectedException.class)
    public ResponseEntity<Map<String, String>> handleEmptyCartDetected(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Cart is empty."));
    }
}
