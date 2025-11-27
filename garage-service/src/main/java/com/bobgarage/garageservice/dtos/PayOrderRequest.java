package com.bobgarage.garageservice.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PayOrderRequest {
    @NotNull(message = "Order ID is required.")
    private UUID orderId;
}
