package com.example.payment_service_app.outbox;


import com.example.payment_service_app.entity.OutboxMessage;
import com.example.payment_service_app.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxRelay {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedDelayString = "${outbox.relay.interval:2000}")
    @Transactional
    public void relay() {
        List<OutboxMessage> msgs = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc(PageRequest.of(0, 20));
        for (OutboxMessage m : msgs) {
            try {
                kafkaTemplate.send(m.getType(), m.getAggregateId(), m.getPayload()).get(); // sync send to ensure ordering
                m.setProcessed(true);
                m.setSentAt(LocalDateTime.now());
                outboxRepository.save(m);
            } catch (Exception e) {
                // log, bırak retry için processed=false kalacak
                // log.debug/ warn
            }
        }
    }
}