package com.example.warehouse_service_app.service;

import com.example.warehouse_service_client.dto.WarehouseAssignmentDto;
import com.example.warehouse_service_client.dto.WarehouseAssignmentRequest;
import com.example.warehouse_service_client.enums.WarehouseRole;

import java.util.List;

public interface WarehouseAssignmentService {
    WarehouseAssignmentDto assignWarehouseAdmin(Long userId, Long warehouseId);
    WarehouseAssignmentDto assignWarehouseManager(Long warehouseId, Long userId);
    void removeAssignment(Long warehouseId , Long userId , WarehouseRole role);
    List<Long> getWarehouseIdsByUser(Long userId);
}
