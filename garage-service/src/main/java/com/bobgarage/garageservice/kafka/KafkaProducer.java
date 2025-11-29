package com.bobgarage.garageservice.kafka;

import com.bobgarage.garageservice.entities.Order;
import customer.events.CustomerEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendPaymentCompleteEvent(Order order) {
        CustomerEvent event = CustomerEvent.newBuilder()
                .setCustomerId(order.getCustomerId().toString())
                .setEventType("ORDER_COMPLETED")
                .setEventDetail(order.getId().toString())
                .build();

        try{
            kafkaTemplate.send("customer", event.toByteArray());
        } catch (Exception e) {
            log.error("Error while sending order event: {}", event);
        }
    }

}
