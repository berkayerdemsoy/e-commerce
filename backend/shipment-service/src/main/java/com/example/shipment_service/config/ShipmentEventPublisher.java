package com.example.shipment_service.config;

import com.example.shipment_service.dto.ShipmentEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.shipmentTopic}")
    private String shipmentTopic;

    public void publishShipmentCreated(ShipmentEventDto event) {
        log.info("Publishing shipment created event: {}", event);
        kafkaTemplate.send(shipmentTopic, event.getShipmentId().toString(), event);
    }
}