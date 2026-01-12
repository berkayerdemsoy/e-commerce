package com.example.warehouse_service_client.client;

import com.example.warehouse_service_client.dto.WarehouseCategoryDto;
import com.example.warehouse_service_client.dto.WarehouseCategoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "warehouse-service",
        contextId = "warehouseCategoryClient",
        path = "/api/warehouse-categories",
        url = "http://api-gateway:8080"
)
public interface WarehouseCategoryClient {

    // Depoya kategori atama
    @PostMapping(value = "/assign", consumes = MediaType.APPLICATION_JSON_VALUE)
    WarehouseCategoryDto assignCategoryToWarehouse(@RequestBody WarehouseCategoryRequest request);

    // Depodan kategori atamasını kaldırma
    @DeleteMapping("/remove/{warehouseId}/{categoryId}")
    WarehouseCategoryDto removeCategoryAssignFromWarehouse(@PathVariable("warehouseId") Long warehouseId,
                                                           @PathVariable("categoryId") Long categoryId);

    // Depodaki kategori atamalarını listeleme
    @GetMapping("/{warehouseId}")
    List<WarehouseCategoryDto> getCategoriesByWarehouse(@PathVariable("warehouseId") Long warehouseId);
}
