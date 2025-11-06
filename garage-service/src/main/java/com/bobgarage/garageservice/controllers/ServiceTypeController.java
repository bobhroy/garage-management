package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.ServiceTypeDto;
import com.bobgarage.garageservice.entities.ServiceType;
import com.bobgarage.garageservice.mappers.ServiceTypeMapper;
import com.bobgarage.garageservice.repositories.ServiceTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/service_types")
public class ServiceTypeController {
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;

    @GetMapping
    public List<ServiceTypeDto> getAllServiceTypes(
        @RequestParam(name = "categoryId", required = false) UUID categoryId
    ) {
        List<ServiceType> services;
        if (categoryId != null) {
            services = serviceTypeRepository.findByCategoryId(categoryId);
        }  else {
            services = serviceTypeRepository.findAllWithCategory();
        }
        return services
                .stream().map(serviceTypeMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceTypeDto> getServiceType(@PathVariable UUID id) {
        var serviceType = serviceTypeRepository.findById(id).orElse(null);
        if (serviceType == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(serviceTypeMapper.toDto(serviceType));
    }
}
