package com.example.order_service_app.config;

import com.example.order_service_app.service.OrderService;
import com.example.order_service_client.dto.OrderResponse;
import com.example.order_service_client.dto.PaymentEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final OrderService orderService;

    @KafkaListener(
            topics = "${app.kafka.topics.paymentTopic}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void handlePaymentEvent(ConsumerRecord<String, String> record, Acknowledgment acknowledgment){

        String message = record.value();
        String key = record.key();
        String eventType = Optional.ofNullable(record.headers().lastHeader("eventType"))
                .map(h -> new String(h.value(), StandardCharsets.UTF_8))
                .orElseGet(() -> {
                    try {
                        return new ObjectMapper().readTree(message).path("eventType").asText(null);
                    } catch (Exception ex) {
                        return null;
                    }
                });

        log.info("Received payment event. key={}, eventType={}", key, eventType);

        try {
            PaymentEventDto event = new ObjectMapper().readValue(message, PaymentEventDto.class);

            if (!"payment.succeeded".equals(event.getEventType())) {
                log.info("Ignoring non-success eventType={} for paymentId={}", event.getEventType(), event.getPaymentId());
                acknowledgment.acknowledge();
                return;
            }

            OrderResponse created = orderService.createOrderFromPayment(event); // idempotency inside
            if (created != null) log.info("Order created for paymentId={}", event.getPaymentId());
            else log.info("Order creation skipped for paymentId={} (idempotent)", event.getPaymentId());

            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Failed to process message key={}, will let error handler/DLQ handle it", key, e);
            throw new RuntimeException(e); // Error handler + DLQ devreye girsin
        }
    }


}
