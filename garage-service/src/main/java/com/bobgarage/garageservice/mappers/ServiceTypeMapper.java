package com.bobgarage.garageservice.mappers;

import com.bobgarage.garageservice.dtos.ServiceTypeDto;
import com.bobgarage.garageservice.entities.ServiceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ServiceTypeDto toDto(ServiceType serviceType);
}
