package com.bobgarage.billingservice.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartSummaryRepository extends JpaRepository<CartItem, UUID> {
    @Query("""
        SELECT new com.bobgarage.billingservice.order.ServiceSummary(st.name, st.price)
        FROM com.bobgarage.billingservice.order.ServiceType st
        WHERE st.id IN (
            SELECT ci.serviceType.id FROM com.bobgarage.billingservice.order.CartItem ci
            WHERE ci.cart.id = :cartId
        )
    """)
    List<ServiceSummary> findSummariesByCartId(UUID cartId);
}
