package com.bobgarage.garageservice.mappers;

import com.bobgarage.garageservice.dtos.ServiceTypeDto;
import com.bobgarage.garageservice.entities.ServiceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ServiceTypeDto toDto(ServiceType serviceType);

    ServiceType toEntity(ServiceTypeDto dto);

    @Mapping(target = "id", ignore = true)
    void update(ServiceTypeDto serviceTypeDto, @MappingTarget ServiceType serviceType);
}
