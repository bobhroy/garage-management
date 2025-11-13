package com.bobgarage.garageservice.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class AddServiceTypeToCartRequest {
    @NotBlank(message = "A product id is required")
    private UUID productId;

    private String technicianName;
}
