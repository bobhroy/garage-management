package com.bobgarage.garageservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerDto {
    public UUID id;
    public String name;
    public String email;
    public String phone;
    public Integer loyaltyPoints;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createdAt;
    public String createdBy;
    public List<AddressDto> addresses;
}
