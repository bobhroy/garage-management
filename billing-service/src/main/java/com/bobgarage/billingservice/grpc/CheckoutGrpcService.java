package com.bobgarage.billingservice.grpc;

import billing.CheckoutRequest;
import billing.CheckoutResponse;
import billing.CheckoutServiceGrpc;
import com.bobgarage.billingservice.order.Order;
import com.bobgarage.billingservice.order.OrderRepository;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

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
}
