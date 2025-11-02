package com.example.shipment_service.client;

import com.example.shipment_service.entity.Shipment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CargoApiClient {

    public CargoAssignmentResult assignShipment(Shipment shipment) {
        log.info("Simulating cargo API call for shipment: {}", shipment.getId());

        // Gerçek kargo API entegrasyonu buraya gelir
        String trackingNumber = "TRACK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String carrier = selectCarrier();

        return CargoAssignmentResult.builder()
                .trackingNumber(trackingNumber)
                .carrierName(carrier)
                .rawResponse("{\"status\":\"success\",\"trackingNo\":\"" + trackingNumber + "\"}")
                .build();
    }

    private String selectCarrier() {
        String[] carriers = {"Yurtiçi Kargo", "MNG Kargo", "Aras Kargo", "PTT Kargo"};
        return carriers[(int) (Math.random() * carriers.length)];
    }
}