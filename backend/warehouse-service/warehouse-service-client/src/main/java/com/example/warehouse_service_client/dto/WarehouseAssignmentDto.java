package com.example.warehouse_service_client.dto;

import com.example.warehouse_service_client.enums.WarehouseRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WarehouseAssignmentDto {
    private Long id;
    private Long userId;
    private Long warehouseId;
    private WarehouseRole role;
    private LocalDateTime assignedAt;
}
