package com.bobgarage.billingservice.grpc;

import billing.CheckoutRequest;
import billing.CheckoutResponse;
import billing.CheckoutServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class CheckoutGrpcService  extends CheckoutServiceGrpc.CheckoutServiceImplBase {

    @Override
    public void checkout(
            CheckoutRequest request,
            StreamObserver<CheckoutResponse> responseObserver
    ){
        log.info("Received checkout request: cartId={}, paymentMethod={}",
                request.getCartId(), request.getPaymentMethod());

        // Execute business logic - e.g save to database

        // Build gRPC response
        CheckoutResponse response = CheckoutResponse.newBuilder()
                .setSuccess(true)
                .setOrderId("5652c9d7-90c9-4011-a438-49d4c6c70fd0")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
