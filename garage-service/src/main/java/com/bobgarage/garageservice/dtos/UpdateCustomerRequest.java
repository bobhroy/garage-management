package com.bobgarage.garageservice.dtos;

import lombok.Data;

@Data
public class UpdateCustomerRequest {
    public String name;
    public String email;
    public String phone;
}
