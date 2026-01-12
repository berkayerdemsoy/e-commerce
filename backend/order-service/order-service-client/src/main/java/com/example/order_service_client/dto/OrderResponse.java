package com.example.order_service_client.dto;

import com.example.order_service_client.enums.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private double totalAmount;
    private StatusType status;
}
