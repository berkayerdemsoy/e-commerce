package com.example.warehouse_service_client.dto;

import lombok.Data;

@Data
public class WarehouseCategoryRequest {
    private Long warehouseId;
    private Long categoryId;
    private Long assignedBy;
}
