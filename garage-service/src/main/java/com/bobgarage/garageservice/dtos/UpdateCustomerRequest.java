package com.bobgarage.garageservice.dtos;

import lombok.Data;

@Data
public class UpdateCustomerRequest {
    private String name;
    private String email;
    private String phone;
}
