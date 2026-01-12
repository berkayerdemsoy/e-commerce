package com.example.payment_service_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxMessage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType; // PAYMENT
    private String aggregateId; // paymentId
    private String type; // payment.succeeded
    @Lob
    private String payload;
    private boolean processed;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
