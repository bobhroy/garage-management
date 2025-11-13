package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.AddServiceTypeToCartRequest;
import com.bobgarage.garageservice.dtos.CartDto;
import com.bobgarage.garageservice.dtos.CartItemDto;
import com.bobgarage.garageservice.entities.Cart;
import com.bobgarage.garageservice.entities.CartItem;
import com.bobgarage.garageservice.mappers.CartMapper;
import com.bobgarage.garageservice.repositories.CartRepository;
import com.bobgarage.garageservice.repositories.ServiceTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddServiceTypeToCartRequest request) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) return ResponseEntity.notFound().build();

        var serviceType = serviceTypeRepository.findById(request.getProductId()).orElse(null);
        if (serviceType == null) return ResponseEntity.badRequest().build();

        var cartItem = cart.getCartItems().stream()
                .filter(item -> item.getServiceType().getId().equals(serviceType.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            return ResponseEntity.badRequest().build();
        }
        else {
            cartItem = new CartItem();
            cartItem.setServiceType(serviceType);
            cartItem.setTechnician(request.getTechnicianName());
            cartItem.setCart(cart);
            cartItem.setStatus("OPEN");
            cart.getCartItems().add(cartItem);
        }
        cartRepository.save(cart);

        var cartItemDto = cartMapper.toDto(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
}
