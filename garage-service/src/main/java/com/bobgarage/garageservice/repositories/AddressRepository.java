package com.bobgarage.garageservice.repositories;

import com.bobgarage.garageservice.entities.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AddressRepository extends CrudRepository<Address, UUID> {
}
