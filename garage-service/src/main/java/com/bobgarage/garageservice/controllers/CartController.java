package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.AddServiceTypeToCartRequest;
import com.bobgarage.garageservice.dtos.CartDto;
import com.bobgarage.garageservice.dtos.CartItemDto;
import com.bobgarage.garageservice.dtos.UpdateCartItemRequest;
import com.bobgarage.garageservice.exceptions.CartNotFoundException;
import com.bobgarage.garageservice.exceptions.DuplicateCartItemException;
import com.bobgarage.garageservice.exceptions.ServiceTypeNotFoundException;
import com.bobgarage.garageservice.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cartDto = cartService.createCart();
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddServiceTypeToCartRequest request) {

        var cartItemDto = cartService.addToCart(cartId, request.getServiceTypeId(), request.getTechnicianName());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{serviceTypeId}")
    public CartItemDto updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("serviceTypeId") UUID serviceTypeId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return cartService.updateItem(cartId, serviceTypeId, request.getTechnician(), request.getStatus());
    }

    @DeleteMapping("/{cartId}/items/{serviceTypeId}")
    public ResponseEntity<?> deleteItemFromCart(
            @PathVariable UUID cartId,
            @PathVariable UUID serviceTypeId
    ) {
        cartService.removeItem(cartId, serviceTypeId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found."));
    }

    @ExceptionHandler(ServiceTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleServiceTypeNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Service type not found in the cart."));
    }

    @ExceptionHandler(DuplicateCartItemException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateItemFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Service type already exist in the cart."));
    }
}
