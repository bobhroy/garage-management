package com.bobgarage.garageservice.dtos;

import lombok.Data;

@Data
public class AddressDto {
    public String street;
    public String city;
    public String zip;
    public String state;
}