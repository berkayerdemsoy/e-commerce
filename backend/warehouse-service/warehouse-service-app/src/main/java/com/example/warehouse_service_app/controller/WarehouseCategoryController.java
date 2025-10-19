package com.example.warehouse_service_app.controller;

import com.example.shop_service_client.dto.CategoryDto;
import com.example.warehouse_service_app.service.WarehouseCategoryService;
import com.example.warehouse_service_client.dto.WarehouseCategoryDto;
import com.example.warehouse_service_client.dto.WarehouseCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouse-categories")
@RequiredArgsConstructor
public class WarehouseCategoryController {

    private final WarehouseCategoryService warehouseCategoryService;


    @PostMapping("/assign")
    public ResponseEntity<WarehouseCategoryDto> assignCategoryToWarehouse(
            @RequestBody WarehouseCategoryRequest request) {
        WarehouseCategoryDto dto = warehouseCategoryService.assignCategoryToWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }


    @DeleteMapping("/remove/{warehouseId}/{categoryId}")
    public ResponseEntity<WarehouseCategoryDto> removeCategoryAssignFromWarehouse(
            @PathVariable("warehouseId") Long warehouseId,
            @PathVariable("categoryId") Long categoryId) {
        WarehouseCategoryDto dto = warehouseCategoryService.removeCategoryAssignFromWarehouse(categoryId, warehouseId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<List<WarehouseCategoryDto>> getCategoriesByWarehouse(@PathVariable("warehouseId") Long warehouseId) {
        List<WarehouseCategoryDto> categories = warehouseCategoryService.getCategoriesByWarehouse(warehouseId);
        return ResponseEntity.ok(categories);
    }

}
