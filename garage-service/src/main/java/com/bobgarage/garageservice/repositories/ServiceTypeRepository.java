package com.bobgarage.garageservice.repositories;

import com.bobgarage.garageservice.entities.ServiceType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ServiceTypeRepository  extends JpaRepository<ServiceType, UUID> {
    @EntityGraph(attributePaths = "category")
    List<ServiceType> findByCategoryId(UUID categoryId);

    @EntityGraph(attributePaths = "category")
    @Query("SELECT s FROM ServiceType s")
    List<ServiceType> findAllWithCategory();
}
