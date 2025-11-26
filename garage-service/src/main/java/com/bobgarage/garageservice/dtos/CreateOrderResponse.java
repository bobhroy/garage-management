package com.bobgarage.garageservice.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateOrderResponse {
    private UUID orderId;
}
