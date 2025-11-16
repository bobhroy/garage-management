package com.bobgarage.garageservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartItemDto {
    private String serviceType;
    private BigDecimal price;
    private String technician;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dateCompleted;
}
