package com.example.warehouse_service_client.client;

import com.example.warehouse_service_client.dto.StockMovementRequest;
import com.example.warehouse_service_client.dto.StockMovementResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "warehouse-service",
        contextId = "stockMovementClient",
        path = "/api/stock-movements",
        url = "http://api-gateway:8080"
)
public interface StockMovementClient {


    @PostMapping("/api/stock-movements")
    StockMovementResponse addStockMovement(@RequestBody StockMovementRequest request);


    @GetMapping("/api/stock-movements/{productId}")
    Page<StockMovementResponse> getAllStockMovements(
            @PathVariable("productId") Long productId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort", required = false) String sort
    );
}
