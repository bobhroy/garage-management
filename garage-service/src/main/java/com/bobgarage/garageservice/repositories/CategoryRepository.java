package com.bobgarage.garageservice.repositories;

import com.bobgarage.garageservice.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CategoryRepository extends CrudRepository<Category, UUID> {
}
