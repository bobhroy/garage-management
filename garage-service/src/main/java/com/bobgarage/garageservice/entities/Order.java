package com.bobgarage.garageservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "cart_id")
    private UUID cartId;

    @Column(name = "status")
    private String status;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDateTime dateCreated;

    @Column(name = "date_updated")
    private LocalDateTime dateUpdated;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    private Set<Invoice> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        return items.stream()
                .filter(item -> item.getServiceTypeName() != null)
                .map(item -> {
                    BigDecimal price = item.getPrice();
                    return price != null ? price : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}