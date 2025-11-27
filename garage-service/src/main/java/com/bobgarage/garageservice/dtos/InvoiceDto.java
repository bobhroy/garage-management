package com.bobgarage.garageservice.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceDto {
    private String serviceTypeName;
    private BigDecimal price;
}
