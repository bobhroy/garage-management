package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.ServiceTypeDto;
import com.bobgarage.garageservice.entities.ServiceType;
import com.bobgarage.garageservice.mappers.ServiceTypeMapper;
import com.bobgarage.garageservice.repositories.CategoryRepository;
import com.bobgarage.garageservice.repositories.ServiceTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/service_types")
public class ServiceTypeController {
    private final ServiceTypeRepository serviceTypeRepository;
    private final ServiceTypeMapper serviceTypeMapper;
    private final CategoryRepository categoryRepository;

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

    @PostMapping
    public ResponseEntity<ServiceTypeDto> createServiceType(
            @RequestBody ServiceTypeDto serviceTypeDto,
            UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.findById(serviceTypeDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        var serviceType = serviceTypeMapper.toEntity(serviceTypeDto);
        serviceType.setCategory(category);
        serviceTypeRepository.save(serviceType);
        serviceTypeDto.setId(serviceType.getId());

        var uri = uriBuilder.path("/service_types/{id}").buildAndExpand(serviceTypeDto.getId()).toUri();

        return ResponseEntity.created(uri).body(serviceTypeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceTypeDto> updateServiceType(
            @PathVariable UUID id,
            @RequestBody ServiceTypeDto serviceTypeDto) {
        var category = categoryRepository.findById(serviceTypeDto.getCategoryId()).orElse(null);
        if (category == null) return ResponseEntity.notFound().build();

        var serviceType = serviceTypeRepository.findById(id).orElse(null);
        if (serviceType == null) return ResponseEntity.notFound().build();

        serviceTypeMapper.update(serviceTypeDto, serviceType);
        serviceType.setCategory(category);
        serviceTypeRepository.save(serviceType);
        serviceTypeDto.setId(serviceType.getId());

        return ResponseEntity.ok(serviceTypeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable UUID id) {
        if (!serviceTypeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        serviceTypeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
