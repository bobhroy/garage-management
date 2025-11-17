package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.ServiceTypeDto;
import com.bobgarage.garageservice.exceptions.ServiceTypeNotFoundException;
import com.bobgarage.garageservice.services.ServiceTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/service_types")
@Tag(name = "Service Types")
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    @GetMapping
    public List<ServiceTypeDto> getAllServiceTypes(
        @RequestParam(name = "categoryId", required = false) UUID categoryId
    ) {
        return serviceTypeService.getAll(categoryId);
    }

    @GetMapping("/{id}")
    public ServiceTypeDto getServiceType(@PathVariable UUID id) {
        return serviceTypeService.getById(id);
    }

    @ExceptionHandler(ServiceTypeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleServiceTypeNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Service type not found in the cart."));
    }
}
