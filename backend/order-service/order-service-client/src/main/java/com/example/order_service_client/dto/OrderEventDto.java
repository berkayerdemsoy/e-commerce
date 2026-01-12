package com.example.order_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEventDto {
    private Long orderId;
    private Long userId;
    private BigDecimal totalAmount;
    private String eventType;
}
