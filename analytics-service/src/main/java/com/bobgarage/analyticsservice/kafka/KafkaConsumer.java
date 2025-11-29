package com.bobgarage.analyticsservice.kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import customer.events.CustomerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(topics="customer", groupId = "analytics-service")
    public void consumeEvent(byte[] event){
        try {
            CustomerEvent customerEvent = CustomerEvent.parseFrom(event);

            // ... need to implement the analytical logic later

            log.info("Received Customer Event: [CustomerId={}, EventType={}, EventDetail={}]",
                    customerEvent.getCustomerId(),
                    customerEvent.getEventType(),
                    customerEvent.getEventDetail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}",e.getMessage());
        }
    }
}
