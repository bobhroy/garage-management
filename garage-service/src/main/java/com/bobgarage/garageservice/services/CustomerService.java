package com.bobgarage.garageservice.services;

import com.bobgarage.garageservice.dtos.AddressDto;
import com.bobgarage.garageservice.dtos.CustomerDto;
import com.bobgarage.garageservice.dtos.RegisterCustomerRequest;
import com.bobgarage.garageservice.dtos.UpdateCustomerRequest;
import com.bobgarage.garageservice.entities.Customer;
import com.bobgarage.garageservice.exceptions.CustomerNotFoundException;
import com.bobgarage.garageservice.exceptions.DuplicateEmailException;
import com.bobgarage.garageservice.mappers.AddressMapper;
import com.bobgarage.garageservice.mappers.CustomerMapper;
import com.bobgarage.garageservice.repositories.AddressRepository;
import com.bobgarage.garageservice.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerService {
    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;
    private AddressMapper addressMapper;
    private AddressRepository addressRepository;

    public Iterable<CustomerDto> getAllCustomers(String sort){
        List<Customer> customers;

        if (sort == null || sort.isEmpty()) {
            customers = customerRepository.findAll();
        }
        else {
            customers = customerRepository.findAllWithAddresses(Sort.by(sort));
        }
        return customers.stream().map(customerMapper::toDto).toList();
    }

    public CustomerDto getCustomerById(UUID id) {
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null) throw new CustomerNotFoundException();
        return customerMapper.toDto(customer);
    }

    public CustomerDto addCustomer(RegisterCustomerRequest request){
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }
        var customer = customerMapper.toEntity(request);
        var address = addressMapper.toEntity(request.getAddress());

        customer.addAddress(address);
        customerRepository.save(customer);

        return customerMapper.toDto(customer);
    }

    public CustomerDto addAddress(UUID customerId, AddressDto addressDto){
        var customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException();

        var address = addressMapper.toEntity(addressDto);
        customer.addAddress(address);
        addressRepository.save(address);

        return customerMapper.toDto(customer);
    }

    public CustomerDto updateCustomer(UUID id, UpdateCustomerRequest request){
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null) throw new  CustomerNotFoundException();

        customerMapper.update(request, customer);
        customerRepository.save(customer);

        return customerMapper.toDto(customer);
    }

    public void deleteCustomer(UUID id){
        var customer = customerRepository.findById(id).orElse(null);
        if (customer == null) throw new  CustomerNotFoundException();
        customerRepository.delete(customer);
    }
}
