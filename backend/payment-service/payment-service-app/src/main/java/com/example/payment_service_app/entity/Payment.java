package com.example.payment_service_app.entity;

import com.example.payment_service_client.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments" , uniqueConstraints = {@UniqueConstraint(name = "uk_payment_idempotency"
        , columnNames = {"idempotency_key"})})
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id",nullable = false,unique = true)
    private UUID paymentId;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "cart_id",nullable = false)
    private Long cartId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "idempotency_key",nullable = false)
    private String idempotencyKey;

    private String providerResponse;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
