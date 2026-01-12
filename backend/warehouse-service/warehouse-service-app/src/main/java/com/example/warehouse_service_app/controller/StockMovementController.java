package com.example.warehouse_service_app.controller;

import com.example.warehouse_service_app.service.StockMovementService;
import com.example.warehouse_service_client.dto.StockMovementRequest;
import com.example.warehouse_service_client.dto.StockMovementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;


    @PostMapping
    public ResponseEntity<StockMovementResponse> addStockMovement(
            @RequestBody StockMovementRequest request) {
        StockMovementResponse response = stockMovementService.addStockMovement(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{productId}")
    public ResponseEntity<Page<StockMovementResponse>> getAllStockMovements(
            @PathVariable("productId") Long productId,
            Pageable pageable) {
        Page<StockMovementResponse> response = stockMovementService.getAllStockMovements(productId, pageable);
        return ResponseEntity.ok(response);
    }
}
