package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.AddressDto;
import com.bobgarage.garageservice.dtos.CustomerDto;
import com.bobgarage.garageservice.dtos.RegisterCustomerRequest;
import com.bobgarage.garageservice.dtos.UpdateCustomerRequest;
import com.bobgarage.garageservice.entities.Customer;
import com.bobgarage.garageservice.mappers.AddressMapper;
import com.bobgarage.garageservice.mappers.CustomerMapper;
import com.bobgarage.garageservice.repositories.AddressRepository;
import com.bobgarage.garageservice.repositories.CustomerRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    @GetMapping
    public Iterable<CustomerDto> getAllCustomers(
            @RequestParam(name = "sort", required = false) String sort
    ) {
        List<Customer> customers;

        if (sort == null || sort.isEmpty()) {
            customers = customerRepository.findAll();
        }
        else {
            customers = customerRepository.findAllWithAddresses(Sort.by(sort));
        }
        return customers.stream().map(customerMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable UUID id) {
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customerMapper.toDto(customer));
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(
            @RequestBody RegisterCustomerRequest request,
            UriComponentsBuilder uriBuilder) {
        var customer = customerMapper.toEntity(request);
        var address = addressMapper.toEntity(request.getAddress());

        customer.addAddress(address);
        customerRepository.save(customer);

        var customerDto = customerMapper.toDto(customer);
        var uri =  uriBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(customerDto);
    }

    @PostMapping("/{id}/add-address")
    public ResponseEntity<CustomerDto> addAddressToCustomer(
            @PathVariable UUID id,
            @Valid @RequestBody AddressDto addressDto,
            UriComponentsBuilder uriBuilder
    ) {
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        var address = addressMapper.toEntity(addressDto);
        address.setCustomer(customer);
        customer.addAddress(address);
        addressRepository.save(address);

        var customerDto = customerMapper.toDto(customer);
        var uri =  uriBuilder.path("/customers/{id}").buildAndExpand(customer.getId()).toUri();

        return ResponseEntity.created(uri).body(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable(name = "id") UUID id,
            @RequestBody UpdateCustomerRequest request) {
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        customerMapper.update(request, customer);
        customerRepository.save(customer);
        return ResponseEntity.ok(customerMapper.toDto(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.delete(customer);
        return ResponseEntity.noContent().build();
    }

}
