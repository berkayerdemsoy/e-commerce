package com.example.warehouse_service_client.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class WarehouseCategoryDto {
    private Long categoryId;
    private Long warehouseId;
    private Long assignedBy;
    private LocalDateTime assignedAt;
    private Boolean isActive;
}
