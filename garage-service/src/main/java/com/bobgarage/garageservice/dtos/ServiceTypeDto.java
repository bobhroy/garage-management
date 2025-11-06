package com.bobgarage.garageservice.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ServiceTypeDto {
    public UUID id;
    public String name;
    public String description;
    public BigDecimal price;
    public Integer durationMinutes;
    public UUID categoryId;
}
