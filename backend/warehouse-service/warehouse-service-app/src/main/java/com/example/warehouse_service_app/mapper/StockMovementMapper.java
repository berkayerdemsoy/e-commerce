package com.example.warehouse_service_app.mapper;

import com.example.warehouse_service_app.entity.StockMovement;
import com.example.warehouse_service_client.dto.StockMovementResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {
    StockMovementResponse toResponse(StockMovement stockMovement);
}
