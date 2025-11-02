package com.example.shipment_service.config;

import com.example.order_service_client.dto.OrderEventDto;
import com.example.shipment_service.service.ShipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

// OrderEventListener.java
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final ShipmentService shipmentService;

    @KafkaListener(
            topics = "${app.kafka.topics.orderTopic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderEvent(@Payload String message, Acknowledgment ack) {
        log.info("Received order event: {}", message);

        try {
            OrderEventDto event = new ObjectMapper().readValue(message, OrderEventDto.class);

            if ("order.confirmed".equals(event.getEventType())) {
                shipmentService.createShipmentFromOrder(event);
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process order event", e);
        }
    }
}
