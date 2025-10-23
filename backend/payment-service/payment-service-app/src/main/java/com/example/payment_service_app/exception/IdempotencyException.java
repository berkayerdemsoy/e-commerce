package com.example.payment_service_app.exception;

import com.example.payment_service_client.enums.PaymentStatus;

import java.util.UUID;

public class IdempotencyException extends RuntimeException {
    private final UUID paymentId;
    private final PaymentStatus status;
    public IdempotencyException(UUID paymentId, PaymentStatus status) {
        super("Request already processed");
        this.paymentId = paymentId;
        this.status = status;
    }
    public UUID getPaymentId() { return paymentId; }
    public PaymentStatus getStatus() { return status; }
}