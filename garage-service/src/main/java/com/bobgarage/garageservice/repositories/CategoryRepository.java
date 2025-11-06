package com.bobgarage.garageservice.repositories;

import com.bobgarage.garageservice.entities.ServiceType;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CategoryRepository extends CrudRepository<ServiceType, UUID> {
}
