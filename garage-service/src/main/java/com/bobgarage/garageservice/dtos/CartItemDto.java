package com.bobgarage.garageservice.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private String serviceType;
    private BigDecimal price;
    private String technician;
    private String status;
}
