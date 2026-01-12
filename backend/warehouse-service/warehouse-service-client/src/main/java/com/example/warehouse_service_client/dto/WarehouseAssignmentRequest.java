package com.example.warehouse_service_client.dto;

import com.example.warehouse_service_client.enums.WarehouseRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseAssignmentRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long warehouseId;

    @NotNull
    private WarehouseRole role;
}
