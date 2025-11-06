package com.bobgarage.garageservice.controllers;

import com.bobgarage.garageservice.dtos.CustomerDto;
import com.bobgarage.garageservice.dtos.RegisterCustomerRequest;
import com.bobgarage.garageservice.entities.Customer;
import com.bobgarage.garageservice.mappers.AddressMapper;
import com.bobgarage.garageservice.mappers.CustomerMapper;
import com.bobgarage.garageservice.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

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
    public CustomerDto addCustomer(@RequestBody RegisterCustomerRequest request) {
        var customer = customerMapper.toEntity(request);
        var address = addressMapper.toEntity(request.getAddress());
        customer.addAddress(address);
        customerRepository.save(customer);

        var customerDto = customerMapper.toDto(customer);
        return customerDto;
    }
}
