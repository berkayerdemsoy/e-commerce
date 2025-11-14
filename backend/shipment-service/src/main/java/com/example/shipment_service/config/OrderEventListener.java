package com.example.shipment_service.config;

import com.example.order_service_client.dto.OrderEventDto;
import com.example.shipment_service.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

    private final ShipmentService shipmentService;

    @KafkaListener(
            topics = "${app.kafka.topics.orderTopic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderEvent(OrderEventDto event, Acknowledgment ack) {
        log.info("Received order event: {}", event);

        try {

            if ("order.confirmed".equals(event.getEventType())) {
                shipmentService.createShipmentFromOrder(event);
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process order event", e);
        }
    }
}
