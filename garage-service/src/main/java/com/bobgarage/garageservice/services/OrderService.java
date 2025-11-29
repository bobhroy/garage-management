package com.bobgarage.garageservice.services;

import billing.CreateResponse;
import billing.ProcessPaymentResponse;
import com.bobgarage.garageservice.dtos.CreateOrderRequest;
import com.bobgarage.garageservice.dtos.OrderDto;
import com.bobgarage.garageservice.dtos.PayOrderRequest;
import com.bobgarage.garageservice.entities.Customer;
import com.bobgarage.garageservice.entities.Order;
import com.bobgarage.garageservice.exceptions.CartNotFoundException;
import com.bobgarage.garageservice.exceptions.CustomerNotFoundException;
import com.bobgarage.garageservice.exceptions.EmptyCartDetectedException;
import com.bobgarage.garageservice.exceptions.OrderNotFoundException;
import com.bobgarage.garageservice.grpc.BillingServiceGrpcClient;
import com.bobgarage.garageservice.kafka.KafkaProducer;
import com.bobgarage.garageservice.mappers.OrderMapper;
import com.bobgarage.garageservice.repositories.CustomerRepository;
import com.bobgarage.garageservice.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@AllArgsConstructor
@Service
public class OrderService {
    private final CartService cartService;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderMapper orderMapper;
    private final KafkaProducer kafkaProducer;

    public CreateResponse createOrder(UUID customerId, CreateOrderRequest request) {
        var customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException();

        var cart = cartService.getCart(request.getCartId());
        if (cart == null) throw new CustomerNotFoundException();

        return billingServiceGrpcClient.createBillingAccount(customer.getId(), cart.getId());
    }

    public ProcessPaymentResponse payOrder(UUID customerId, PayOrderRequest request) {
        var customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) throw new CustomerNotFoundException();

        var order = orderRepository.findByIdAndCustomerId(
                request.getOrderId(),
                customer.getId())
                .orElse(null);
        if (order == null) throw new OrderNotFoundException();

        var cart = cartService.getCart(order.getCartId());
        if (cart == null) throw new CartNotFoundException();
        if(cart.getItems().isEmpty()) throw new EmptyCartDetectedException();

        var response = billingServiceGrpcClient.processPayment(order.getId());

        if(response.getStatus().equalsIgnoreCase("paid")){

            kafkaProducer.sendPaymentCompleteEvent(order);

            cartService.clearCart(order.getCartId());
        }

        return response;
    }

    public OrderDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(CustomerNotFoundException::new);

        return orderMapper.toDto(order, customer);
    }
}
