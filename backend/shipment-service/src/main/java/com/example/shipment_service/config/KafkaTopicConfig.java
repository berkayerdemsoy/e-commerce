package com.example.shipment_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${app.kafka.topics.shipmentTopic}")
    private String shipmentTopic;

    @Bean
    public NewTopic shipmentTopic() {
        return TopicBuilder.name(shipmentTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic shipmentTopicDlq() {
        return TopicBuilder.name(shipmentTopic + ".DLQ")
                .partitions(1)
                .replicas(1)
                .build();
    }
}

