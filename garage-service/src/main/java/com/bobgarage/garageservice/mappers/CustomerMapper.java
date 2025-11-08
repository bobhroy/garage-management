package com.bobgarage.garageservice.mappers;

import com.bobgarage.garageservice.dtos.CustomerDto;
import com.bobgarage.garageservice.dtos.RegisterCustomerRequest;
import com.bobgarage.garageservice.dtos.UpdateCustomerRequest;
import com.bobgarage.garageservice.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface CustomerMapper {
    CustomerDto toDto(Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "loyaltyPoints", constant = "0")
    Customer toEntity(RegisterCustomerRequest request);

    void update(UpdateCustomerRequest request, @MappingTarget Customer customer);
}
