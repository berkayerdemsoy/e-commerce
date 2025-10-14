package com.example.warehouse_service.service;

import com.example.warehouse_service.entity.WarehouseRole;

import java.util.List;

public interface WarehouseAssignmentService {
    void assignWarehouseAdmin(Long userId, Long warehouseId);
    void assignWarehouseManager(Long warehouseId,Long userId);
    void removeAssignment(Long warehouseId , Long userId , WarehouseRole role);
    List<Long> getWarehouseIdsByUser(Long userId);
}
