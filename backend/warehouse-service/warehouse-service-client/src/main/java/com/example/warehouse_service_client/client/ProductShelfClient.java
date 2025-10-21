package com.example.warehouse_service_client.client;

import com.example.warehouse_service_client.dto.ProductShelfFilterRequest;
import com.example.warehouse_service_client.dto.ProductShelfResponse;
import com.example.warehouse_service_client.dto.ProductShelfUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "warehouse-service",
        contextId = "productShelfClient",
        path = "/api/product-shelves",
        url = "http://api-gateway:8080"
)
public interface ProductShelfClient {


    @PostMapping("/assign")
    ProductShelfResponse assignProductToWarehouse(@RequestBody ProductShelfFilterRequest request);


    @PutMapping("/update-stock")
    ProductShelfResponse updateStock(@RequestBody ProductShelfUpdateRequest request);

    @GetMapping("/low-stock/{warehouseId}")
    Page<ProductShelfResponse> getLowStockAlerts(
            @PathVariable("warehouseId") Long warehouseId,
            Pageable pageable
    );
}
