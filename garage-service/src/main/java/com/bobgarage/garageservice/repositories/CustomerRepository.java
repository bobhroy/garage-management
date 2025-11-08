package com.bobgarage.garageservice.repositories;

import com.bobgarage.garageservice.entities.Customer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.addresses")
    List<Customer> findAllWithAddresses(Sort sort);

    boolean existsByEmail(String email);
}
