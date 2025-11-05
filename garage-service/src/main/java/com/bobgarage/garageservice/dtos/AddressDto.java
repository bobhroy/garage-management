package com.bobgarage.garageservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddressDto {
    public String street;
    public String city;
    public String zip;
    public String state;
}