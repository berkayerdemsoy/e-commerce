package com.example.shipment_service.service;

import com.example.order_service_client.dto.OrderEventDto;
import com.example.shipment_service.dto.ShipmentResponse;

public interface ShipmentService {
    ShipmentResponse createShipmentFromOrder(OrderEventDto orderEvent);
}
