package com.example.shipment_service.mapper;

import com.example.shipment_service.dto.ShipmentResponse;
import com.example.shipment_service.entity.Shipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {
    ShipmentResponse toDto(Shipment shipment);
}
