package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseCategoryDto {
    private Long categoryId;
    private Long warehouseId;
    private Long assignedBy;
    private LocalDateTime assignedAt;
    private Boolean isActive;
}
