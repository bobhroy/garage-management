package com.bobgarage.garageservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class AddServiceTypeToCartRequest {
    @NotBlank(message = "An id of the service type is required")
    private UUID serviceTypeId;

    @Size(max = 255, message = "Name must be less than 255 characters")
    private String technicianName;
}
