package com.example.warehouse_service_app.controller;

import com.example.warehouse_service_app.service.ProductShelfService;
import com.example.warehouse_service_client.dto.ProductShelfFilterRequest;
import com.example.warehouse_service_client.dto.ProductShelfResponse;
import com.example.warehouse_service_client.dto.ProductShelfUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-shelves")
@RequiredArgsConstructor
public class ProductShelfController {

    private final ProductShelfService productShelfService;


    @PostMapping("/assign")
    public ResponseEntity<ProductShelfResponse> assignProductToWarehouse(
            @RequestBody ProductShelfFilterRequest request) {
        ProductShelfResponse response = productShelfService.assignProductToWarehouse(request);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/update-stock")
    public ResponseEntity<ProductShelfResponse> updateStock(
            @RequestBody ProductShelfUpdateRequest request) {
        ProductShelfResponse response = productShelfService.updateStock(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/low-stock/{warehouseId}")
    public ResponseEntity<Page<ProductShelfResponse>> getLowStockAlerts(
            @PathVariable("warehouseId") Long warehouseId,
            Pageable pageable) {
        Page<ProductShelfResponse> response = productShelfService.getLowStockAlerts(warehouseId, pageable);
        return ResponseEntity.ok(response);
    }
}

