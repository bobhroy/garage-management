package com.bobgarage.garageservice.mappers;

import com.bobgarage.garageservice.dtos.CustomerDto;
import com.bobgarage.garageservice.entities.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);
}
