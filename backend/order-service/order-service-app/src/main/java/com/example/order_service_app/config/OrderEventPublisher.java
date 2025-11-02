package com.example.order_service_app.config;

import com.example.order_service_client.dto.OrderEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    @Value("${app.kafka.topics.orderTopic}")
    private String orderTopic;

    public void publishOrderConfirmed(OrderEventDto event){
        log.info("Publishing order confirmed event: {}", event);
        kafkaTemplate.send(orderTopic,event.getOrderId().toString(),event);
    }
}
