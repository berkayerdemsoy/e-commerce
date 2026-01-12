package com.example.shipment_service.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CargoAssignmentResult {
    private String trackingNumber;
    private String carrierName;
    private String rawResponse;
}