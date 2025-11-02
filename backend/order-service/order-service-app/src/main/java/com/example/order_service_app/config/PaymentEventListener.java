package com.example.order_service_app.config;

import com.example.order_service_app.service.OrderService;
import com.example.order_service_client.dto.PaymentEventDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

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
    public void handlePaymentEvent(@Payload String message , Acknowledgment acknowledgment){
        log.info("Received payment event message: {}", message);

        try {
            PaymentEventDto event = parsePaymentEvent(message);
            if("payment.succeeded".equals(event.getEventType())){
                orderService.createOrderFromPayment(event);
                log.info("Order Created for Payment ID: {}", event.getPaymentId());
            }
            acknowledgment.acknowledge();

        }catch (Exception e){
            log.error("Failed to process payment event message: {}", message, e);

        }

    }

    private PaymentEventDto parsePaymentEvent(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json,PaymentEventDto.class);
    }

}
