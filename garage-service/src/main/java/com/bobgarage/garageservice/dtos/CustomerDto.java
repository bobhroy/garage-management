package com.bobgarage.garageservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private Integer loyaltyPoints;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String createdBy;
    private List<AddressDto> addresses;
}
