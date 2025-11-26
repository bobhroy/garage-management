package com.bobgarage.garageservice.grpc;

import billing.BillingServiceGrpc;
import billing.CreateRequest;
import billing.CreateResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class BillingServiceGrpcClient {
    public final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;

    public BillingServiceGrpcClient(
            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort) {

        log.info("Connecting to Billing Service GRPC service at {}:{}",
                serverAddress, serverPort);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress,
                serverPort).usePlaintext().build();

        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public CreateResponse createBillingAccount(UUID customerId, UUID cartId) {
        CreateRequest request = CreateRequest
                .newBuilder()
                .setCustomerId(customerId.toString())
                .setCartId(cartId.toString())
                .build();
        CreateResponse response = blockingStub.create(request);
        log.info("Received response from billing service via GRPC: {}", response);
        return response;
    }
}
