package com.bobgarage.garageservice.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ServiceTypeDto {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private UUID categoryId;
}
