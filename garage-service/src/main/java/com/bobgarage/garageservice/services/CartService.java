package com.bobgarage.garageservice.services;

import com.bobgarage.garageservice.dtos.CartDto;
import com.bobgarage.garageservice.dtos.CartItemDto;
import com.bobgarage.garageservice.entities.Cart;
import com.bobgarage.garageservice.exceptions.CartNotFoundException;
import com.bobgarage.garageservice.exceptions.ServiceTypeNotFoundException;
import com.bobgarage.garageservice.mappers.CartMapper;
import com.bobgarage.garageservice.repositories.CartRepository;
import com.bobgarage.garageservice.repositories.ServiceTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ServiceTypeRepository serviceTypeRepository;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, UUID serviceTypeId, String technician) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        var serviceType = serviceTypeRepository.findById(serviceTypeId).orElse(null);
        if (serviceType == null) throw new ServiceTypeNotFoundException();

        var cartItem = cart.addItem(serviceType, technician);

        cartRepository.save(cart);
        return cartMapper.toDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        return cartMapper.toDto(cart);
    }

    public void removeItem(UUID cartId, UUID serviceTypeId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        cart.removeItem(serviceTypeId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) throw new CartNotFoundException();

        cart.clear();
        cartRepository.save(cart);
    }

}
