package com.bobgarage.garageservice.dtos;

import lombok.Data;

@Data
public class RegisterCustomerRequest {
    private String name;
    private String email;
    private String phone;
    private String createdBy;
    private AddressDto address;
}
