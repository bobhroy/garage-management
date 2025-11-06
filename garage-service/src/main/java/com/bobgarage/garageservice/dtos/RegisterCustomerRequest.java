package com.bobgarage.garageservice.dtos;

import lombok.Data;

@Data
public class RegisterCustomerRequest {
    public String name;
    public String email;
    public String phone;
    public String createdBy;
    public AddressDto address;
}
