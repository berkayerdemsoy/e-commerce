package com.example.payment_service_app.outbox;


import com.example.payment_service_app.entity.OutboxMessage;
import com.example.payment_service_app.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {

    @Qualifier("kafkaPaymentTemplate")
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;

    @Value("${app.kafka.topics.paymentTopic}")
    private String paymentTopic;

    @Scheduled(fixedDelayString = "${outbox.relay.interval:30000}")
    @Transactional
    public void relay() {
        List<OutboxMessage> msgs = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc(PageRequest.of(0, 20));
        for (OutboxMessage m : msgs) {
            String key = m.getAggregateId();
            String payload = m.getPayload();
            String eventType = m.getType(); // "payment.succeeded" veya "payment.failed"

            ProducerRecord<String, String> record = new ProducerRecord<>(paymentTopic, null, key, payload, null);
            record.headers().add("eventType", eventType.getBytes(StandardCharsets.UTF_8));

            try {
                // Sync send to ensure ordering per partition (blocks current thread)
                kafkaTemplate.send(record).get();
                m.setProcessed(true);
                m.setSentAt(LocalDateTime.now());
                outboxRepository.save(m);
                log.info("Relayed outbox id={} to topic={} key={} eventType={}", m.getId(), paymentTopic, key, eventType);
            } catch (Exception e) {
                // keep processed = false for retry; log error
                log.warn("Failed to relay outbox id={} to kafka, will retry later", m.getId(), e);
            }
        }
    }
}
