package com.bobgarage.garageservice.services;

import com.bobgarage.garageservice.dtos.ServiceTypeDto;
import com.bobgarage.garageservice.entities.ServiceType;
import com.bobgarage.garageservice.exceptions.ServiceTypeNotFoundException;
import com.bobgarage.garageservice.mappers.ServiceTypeMapper;
import com.bobgarage.garageservice.repositories.ServiceTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ServiceTypeService {
    private ServiceTypeRepository serviceTypeRepository;
    private ServiceTypeMapper serviceTypeMapper;

    public List<ServiceTypeDto> getAll(UUID categoryId){
        List<ServiceType> services;
        if (categoryId != null) {
            services = serviceTypeRepository.findByCategoryId(categoryId);
        }  else {
            services = serviceTypeRepository.findAllWithCategory();
        }
        return services
                .stream().map(serviceTypeMapper::toDto).toList();
    }

    public ServiceTypeDto getById(UUID id) {
        var serviceType = serviceTypeRepository.findById(id).orElse(null);
        if (serviceType == null) throw new ServiceTypeNotFoundException();
        return serviceTypeMapper.toDto(serviceType);
    }
}
