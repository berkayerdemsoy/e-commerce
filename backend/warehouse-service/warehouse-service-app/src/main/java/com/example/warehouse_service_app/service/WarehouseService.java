package com.example.warehouse_service_app.service;

import com.example.warehouse_service_client.dto.WarehouseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    Page<WarehouseDto> getAllWarehouses(Pageable pageable);
    WarehouseDto getWarehouseById(Long id);
    WarehouseDto getWarehouseByName(String name);
    WarehouseDto createWarehouse(WarehouseDto warehouseDto);
    WarehouseDto updateWarehouse(Long id,WarehouseDto warehouseDto);
    Void deleteWarehouse(Long id);
}
