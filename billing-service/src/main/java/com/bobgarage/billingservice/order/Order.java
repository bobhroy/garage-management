package com.bobgarage.billingservice.order;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Invoice> items = new LinkedHashSet<>();

    public Invoice getItem(String serviceTypeName){
        return items.stream()
                .filter(item -> item.getServiceTypeName().equalsIgnoreCase(serviceTypeName))
                .findFirst()
                .orElse(null);
    }

    public void addItem(String serviceTypeName, BigDecimal price){
        var item = getItem(serviceTypeName);
        if(item != null) return; // don't add duplicate items

        item = new Invoice();
        item.setOrder(this);
        item.setServiceTypeName(serviceTypeName);
        item.setPrice(price);
        items.add(item);
    }
}