package com.example.shipment_service.service;

import com.example.order_service_client.dto.OrderEventDto;
import com.example.shipment_service.dto.ShipmentEventDto;
import com.example.shipment_service.dto.ShipmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipmentService {
    ShipmentResponse createShipmentFromOrder(OrderEventDto orderEvent);
    Page<ShipmentResponse> getAllShipments(Pageable pageable , Long userId);
}
