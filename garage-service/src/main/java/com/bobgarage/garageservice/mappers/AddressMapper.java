package com.bobgarage.garageservice.mappers;

import com.bobgarage.garageservice.dtos.AddressDto;
import com.bobgarage.garageservice.entities.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Address toEntity(AddressDto dto);
}
