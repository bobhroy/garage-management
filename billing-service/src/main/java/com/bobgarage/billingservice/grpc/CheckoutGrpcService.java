package com.bobgarage.billingservice.grpc;

import billing.*;
import com.bobgarage.billingservice.order.Order;
import com.bobgarage.billingservice.order.OrderRepository;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@GrpcService
@AllArgsConstructor
public class CheckoutGrpcService extends CheckoutServiceGrpc.CheckoutServiceImplBase {

    private final OrderRepository orderRepository;

    @Override
    public void checkout(
        CheckoutRequest request,
        StreamObserver<CheckoutResponse> responseObserver
    ){
        log.info("Received checkout request: customerId={}",
                request.getCustomerId());

        var order = new Order();
        order.setCustomerId(UUID.fromString(request.getCustomerId()));
        order.setCartId(UUID.fromString(request.getCartId()));
        order.setStatus("CREATED");
        var newOrder = orderRepository.save(order);

        // Build gRPC response
        CheckoutResponse response = CheckoutResponse.newBuilder()
                .setOrderId(newOrder.getId().toString())
                .setStatus(newOrder.getStatus())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void processPayment(
            ProcessPaymentRequest request,
            StreamObserver<ProcessPaymentResponse> responseObserver
    ) {
        log.info("Received payment process request: orderId={}",
                request.getOrderId());

        // Needs more refactoring for later to handle edge cases
        ProcessPaymentResponse response = ProcessPaymentResponse.newBuilder()
                .setOrderId(request.getOrderId())
                .setStatus("Order Not Found")
                .build();

        Order updatedOrder = orderRepository.findById(UUID.fromString(request.getOrderId()))
                .map(order -> {
                    order.setStatus("PAID");
                    order.setDateUpdated(LocalDateTime.now());
                    return orderRepository.save(order);
                })
                .orElse(null);

        if(updatedOrder != null){
            response = ProcessPaymentResponse.newBuilder()
                    .setOrderId(updatedOrder.getId().toString())
                    .setStatus(updatedOrder.getStatus())
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
