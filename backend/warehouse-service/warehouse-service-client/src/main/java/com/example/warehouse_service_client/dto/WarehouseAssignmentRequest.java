package com.example.warehouse_service_client.dto;

import com.example.warehouse_service_client.enums.WarehouseRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseAssignmentRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long warehouseId;

    @NotNull
    private WarehouseRole role;
}
