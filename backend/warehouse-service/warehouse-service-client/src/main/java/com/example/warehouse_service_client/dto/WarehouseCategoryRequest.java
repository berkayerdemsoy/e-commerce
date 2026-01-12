package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseCategoryRequest {
    private Long warehouseId;
    private Long categoryId;
    private Long assignedBy;
}
