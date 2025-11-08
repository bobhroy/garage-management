package com.bobgarage.garageservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ServiceTypeDto {
    private UUID id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Price is required")
    private BigDecimal price;

    @NotBlank(message = "Duration in minutes is required")
    private Integer durationMinutes;

    @NotBlank(message = "Category Id is required")
    private UUID categoryId;
}
