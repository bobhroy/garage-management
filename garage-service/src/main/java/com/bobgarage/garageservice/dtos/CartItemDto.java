package com.bobgarage.garageservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CartItemDto {
    private UUID serviceTypeId;
    private String serviceType;
    private BigDecimal price;
    private String technician;
    private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dateCompleted;
}
