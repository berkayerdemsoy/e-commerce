package com.example.warehouse_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AisleDto {
    private Long id;
    @NotNull(message = "aisle code is required")
    private String aisleCode;
    private Long warehouseId;
    private Long categoryId;
}
