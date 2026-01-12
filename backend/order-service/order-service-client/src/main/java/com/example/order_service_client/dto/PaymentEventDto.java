package com.example.order_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEventDto {
    private String paymentId;
    private Long userId;
    private Long cartId;
    private double amount;
    private String status;
    private String eventType;
}
