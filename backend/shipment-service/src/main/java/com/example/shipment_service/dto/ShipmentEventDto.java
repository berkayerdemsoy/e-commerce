package com.example.shipment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentEventDto implements Serializable {
    private Long shipmentId;
    private Long orderId;
    private Long userId;
    private String trackingNumber;
    private String carrierName;
    private String eventType; // "shipment.created", "shipment.delivered"
}