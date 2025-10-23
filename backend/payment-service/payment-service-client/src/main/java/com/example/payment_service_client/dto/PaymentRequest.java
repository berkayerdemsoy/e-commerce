package com.example.payment_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String cartId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String idempotencyKey;
}
