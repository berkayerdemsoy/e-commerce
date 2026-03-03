package com.example.warehouse_service_app.service;


import com.example.warehouse_service_client.dto.ProductShelfFilterRequest;
import com.example.warehouse_service_client.dto.ProductShelfResponse;
import com.example.warehouse_service_client.dto.ProductShelfUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductShelfService {
    ProductShelfResponse assignProductToWarehouse(ProductShelfFilterRequest dto);
    ProductShelfResponse updateStock(ProductShelfUpdateRequest request);
    Page<ProductShelfResponse> getLowStockAlerts(Long warehouseId, Pageable pageable);
    Page<ProductShelfResponse> getByWarehouseId(Long warehouseId, Pageable pageable);
}
