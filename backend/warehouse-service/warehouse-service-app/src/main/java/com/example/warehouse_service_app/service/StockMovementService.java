package com.example.warehouse_service_app.service;

import com.example.warehouse_service_client.dto.StockMovementRequest;
import com.example.warehouse_service_client.dto.StockMovementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StockMovementService {
    StockMovementResponse addStockMovement(StockMovementRequest stockMovementRequest);
    Page<StockMovementResponse> getAllStockMovements(Long productId, Pageable pageable);
}
