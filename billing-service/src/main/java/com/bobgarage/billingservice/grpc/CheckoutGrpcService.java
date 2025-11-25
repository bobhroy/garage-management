package com.bobgarage.billingservice.grpc;

import billing.*;
import com.bobgarage.billingservice.order.Order;
import com.bobgarage.billingservice.order.OrderNotFoundException;
import com.bobgarage.billingservice.order.OrderService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@AllArgsConstructor
public class CheckoutGrpcService extends CheckoutServiceGrpc.CheckoutServiceImplBase {

    private final OrderService orderService;

    @Override
    public void checkout(
        CheckoutRequest request,
        StreamObserver<CheckoutResponse> responseObserver
    ){
        log.info("Received checkout request: customerId={}",
                request.getCustomerId());

        var order = orderService.createOrder(
                request.getCustomerId(),
                request.getCartId());

        // Build gRPC response
        CheckoutResponse response = CheckoutResponse.newBuilder()
                .setOrderId(order.getId().toString())
                .setStatus(order.getStatus())
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
        try {
            Order order = orderService.markAsPaid(request.getOrderId());

            ProcessPaymentResponse response = ProcessPaymentResponse.newBuilder()
                    .setOrderId(order.getId().toString())
                    .setStatus(order.getStatus())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }catch (OrderNotFoundException e) {
            responseObserver.onError(
                    Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL.withDescription("Unexpected error: " + e.getMessage()).asRuntimeException()
            );
        }
    }
}
