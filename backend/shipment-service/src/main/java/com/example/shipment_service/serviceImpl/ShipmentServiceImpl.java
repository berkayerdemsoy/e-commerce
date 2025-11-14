package com.example.shipment_service.serviceImpl;

import com.example.order_service_client.dto.OrderEventDto;
import com.example.shipment_service.client.CargoApiClient;
import com.example.shipment_service.client.CargoAssignmentResult;
import com.example.shipment_service.config.ShipmentEventPublisher;
import com.example.shipment_service.dto.ShipmentEventDto;
import com.example.shipment_service.dto.ShipmentResponse;
import com.example.shipment_service.entity.Shipment;
import com.example.shipment_service.enums.ShipmentStatus;
import com.example.shipment_service.mapper.ShipmentMapper;
import com.example.shipment_service.repository.ShipmentRepository;
import com.example.shipment_service.service.ShipmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

// ShipmentServiceImpl.java
@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentEventPublisher eventPublisher;
    private final CargoApiClient cargoApiClient;
    private final ShipmentMapper shipmentMapper;



    @Override
    @Transactional
    public ShipmentResponse createShipmentFromOrder(OrderEventDto orderEvent) {
        log.info("Creating shipment for order: {}", orderEvent.getOrderId());

        // 1. Shipment kaydı oluştur
        Shipment shipment = Shipment.builder()
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .status(ShipmentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        shipment = shipmentRepository.save(shipment);

        // 2. Kargo API'sine istek at (simülasyon)
        CargoAssignmentResult result = cargoApiClient.assignShipment(shipment);

        shipment.setStatus(ShipmentStatus.ASSIGNED);
        shipment.setTrackingNumber(result.getTrackingNumber());
        shipment.setCarrierName(result.getCarrierName());
        shipment.setCarrierResponse(result.getRawResponse());
        shipmentRepository.save(shipment);

        // 3. Notification Service'e event gönder
        ShipmentEventDto event = ShipmentEventDto.builder()
                .shipmentId(shipment.getId())
                .orderId(orderEvent.getOrderId())
                .userId(orderEvent.getUserId())
                .trackingNumber(result.getTrackingNumber())
                .eventType("shipment.created")
                .build();

        eventPublisher.publishShipmentCreated(event);

        return shipmentMapper.toDto(shipment);
    }

    @Override
    public Page<ShipmentResponse> getAllShipments(Pageable pageable, Long userId) {
        Page<Shipment> shipments = shipmentRepository.findByUserId(userId,pageable);
        Page<ShipmentResponse> eventDtos = shipments.map(shipmentMapper::toDto);
        return eventDtos;

    }
}
