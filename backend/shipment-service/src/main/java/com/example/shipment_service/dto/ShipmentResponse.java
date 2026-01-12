package com.example.shipment_service.dto;

import com.example.shipment_service.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentResponse {
    private Long id;
    private Long orderId;
    private Long userId;
    private ShipmentStatus status;
    private String trackingNumber;
    private String carrierName;
    private LocalDateTime createdAt;
}