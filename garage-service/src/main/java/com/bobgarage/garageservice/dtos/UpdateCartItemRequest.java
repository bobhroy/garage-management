package com.bobgarage.garageservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String technician;

    @NotBlank(message = "Service tasks status is required")
    @Pattern(
            regexp = "OPEN|IN-PROGRESS|COMPLETED|CANCELLED",
            message = "Status must be one of: OPEN, IN-PROGRESS, COMPLETED, CANCELLED"
    )
    private String status;
}
