package com.bobgarage.billingservice.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartSummaryRepository cartSummaryRepository;

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

        var summaries = cartSummaryRepository.findSummariesByCartId(order.getCartId());
        if(summaries.isEmpty()) {
            throw new OrderNotFoundException(orderId);
        }
        summaries.forEach(summary ->
                order.addItem(summary.serviceTypeName(), summary.price())
        );

        order.setStatus("PAID");
        order.setDateUpdated(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
