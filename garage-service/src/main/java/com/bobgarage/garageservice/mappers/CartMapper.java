package com.bobgarage.garageservice.mappers;

import com.bobgarage.garageservice.dtos.CartDto;
import com.bobgarage.garageservice.dtos.CartItemDto;
import com.bobgarage.garageservice.entities.Cart;
import com.bobgarage.garageservice.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(source = "serviceType.id", target = "serviceTypeId")
    @Mapping(source = "serviceType.name", target = "serviceType")
    @Mapping(source = "serviceType.price", target = "price")
    CartItemDto toDto(CartItem cartItem);
}
