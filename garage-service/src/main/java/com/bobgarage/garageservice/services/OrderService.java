package com.bobgarage.garageservice.services;

import billing.CreateResponse;
import billing.ProcessPaymentResponse;
import com.bobgarage.garageservice.dtos.CreateOrderRequest;
import com.bobgarage.garageservice.dtos.PayOrderRequest;
import com.bobgarage.garageservice.exceptions.CustomerNotFoundException;
import com.bobgarage.garageservice.exceptions.OrderNotFoundException;
import com.bobgarage.garageservice.grpc.BillingServiceGrpcClient;
import com.bobgarage.garageservice.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class OrderService {
    private final CustomerService customerService;
    private final CartService cartService;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final OrderRepository orderRepository;

    public CreateResponse createOrder(UUID customerId, CreateOrderRequest request) {
        var customer = customerService.getCustomerById(customerId);
        if (customer == null) throw new CustomerNotFoundException();

        var cart = cartService.getCart(request.getCartId());
        if (cart == null) throw new CustomerNotFoundException();

        return billingServiceGrpcClient.createBillingAccount(customer.getId(), cart.getId());
    }

    public ProcessPaymentResponse payOrder(UUID customerId, PayOrderRequest request) {
        var customer = customerService.getCustomerById(customerId);
        if (customer == null) throw new CustomerNotFoundException();

        var order = orderRepository.findByIdAndCustomerId(request.getOrderId(), customer.getId()).orElse(null);
        if (order == null) throw new OrderNotFoundException();

        return billingServiceGrpcClient.processPayment(order.getId());
    }
}
