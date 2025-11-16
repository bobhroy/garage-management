package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.AddServiceTypeToCartRequest;
import com.bobgarage.garageservice.dtos.CartDto;
import com.bobgarage.garageservice.dtos.UpdateCartItemRequest;
import com.bobgarage.garageservice.entities.Cart;
import com.bobgarage.garageservice.mappers.CartMapper;
import com.bobgarage.garageservice.repositories.CartRepository;
import com.bobgarage.garageservice.repositories.ServiceTypeRepository;
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
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ServiceTypeRepository serviceTypeRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ) {
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<?> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddServiceTypeToCartRequest request) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("error", "Cart does not exist. Create a cart to add service types.")
        );

        var serviceType = serviceTypeRepository.findById(request.getServiceTypeId()).orElse(null);
        if (serviceType == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Unable to find service type.")
            );
        }

        var cartItem = cart.addItem(serviceType, request.getTechnicianName());
        if (cartItem == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Service type is already added to the cart")
            );
        }

        cartRepository.save(cart);
        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<?> getCart(@PathVariable UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );
        }
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{serviceTypeId}")
    public ResponseEntity<?> updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("serviceTypeId") UUID serviceTypeId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );
        }

        var cartItem = cart.getItem(serviceTypeId);
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Service type not found in the cart.")
            );
        }
        cartItem.setTechnician(request.getTechnician());
        cartItem.setStatus(request.getStatus());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{serviceTypeId}")
    public ResponseEntity<?> deleteCart(
            @PathVariable UUID cartId,
            @PathVariable UUID serviceTypeId
    ) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );
        }

        cart.removeItem(serviceTypeId);
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );
        }

        cart.clear();
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }
}
