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
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date_created", insertable = false, updatable = false)
    private LocalDateTime dateCreated;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        return items.stream()
                .filter(item -> item.getServiceType() != null)
                .map(item -> {
                    BigDecimal price = item.getServiceType().getPrice();
                    return price != null ? price : BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public CartItem getItem(UUID serviceTypeId){
        return items.stream()
                .filter(item -> item.getServiceType().getId().equals(serviceTypeId))
                .findFirst()
                .orElse(null);
    }

    public CartItem addItem(ServiceType serviceType, String technician){
        var cartItem = getItem(serviceType.getId());

        // if item doesn't exist already
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setServiceType(serviceType);
            cartItem.setTechnician(technician);
            cartItem.setStatus("OPEN");
            cartItem.setCart(this);
            items.add(cartItem);

            return cartItem;
        }

        return null;
    }

    public void removeItem(UUID serviceTypeId){
        var cartItem = getItem(serviceTypeId);
        if (cartItem != null) {
            items.remove(cartItem);
            cartItem.setCart(null);
        }
    }

    public void clear(){
        items.clear();
    }
}