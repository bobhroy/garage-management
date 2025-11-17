package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.AddressDto;
import com.bobgarage.garageservice.dtos.CustomerDto;
import com.bobgarage.garageservice.dtos.RegisterCustomerRequest;
import com.bobgarage.garageservice.dtos.UpdateCustomerRequest;
import com.bobgarage.garageservice.exceptions.CustomerNotFoundException;
import com.bobgarage.garageservice.exceptions.DuplicateEmailException;
import com.bobgarage.garageservice.services.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
@Tag(name = "Customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public Iterable<CustomerDto> getAllCustomers(
            @RequestParam(name = "sort", required = false) String sort
    ) {
        return customerService.getAllCustomers(sort);
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomer(@PathVariable UUID id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> registerCustomer(
            @Valid @RequestBody RegisterCustomerRequest request,
            UriComponentsBuilder uriBuilder) {

        var customerDto = customerService.addCustomer(request);
        var uri =  uriBuilder.path("/customers/{id}").buildAndExpand(customerDto.getId()).toUri();

        return ResponseEntity.created(uri).body(customerDto);
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<CustomerDto> addAddressToCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody AddressDto addressDto,
            UriComponentsBuilder uriBuilder
    ) {
        var customerDto = customerService.addAddress(id, addressDto);
        var uri =  uriBuilder.path("/customers/{id}").buildAndExpand(customerDto.getId()).toUri();

        return ResponseEntity.created(uri).body(customerDto);
    }

    @PutMapping("/{id}")
    public CustomerDto updateCustomer(
            @PathVariable(name = "id") UUID id,
            @RequestBody UpdateCustomerRequest request) {

        return customerService.updateCustomer(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerNotFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Customer not found."));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmailFound(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email is already registered."));
    }
}
