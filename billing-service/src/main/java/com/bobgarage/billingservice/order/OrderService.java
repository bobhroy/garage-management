package com.bobgarage.billingservice.order;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(String customerId, String cartId) {
        Order newOrder = new Order();
        newOrder.setCustomerId(UUID.fromString(customerId));
        newOrder.setCartId(UUID.fromString(cartId));
        newOrder.setStatus("CREATED");
        return orderRepository.save(newOrder);
    }

    public Order markAsPaid(String orderId) {
        Order order = orderRepository.findById(UUID.fromString(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus("PAID");
        order.setDateUpdated(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
