package com.example.warehouse_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WarehouseDto {
    private Long id;
    @NotNull(message = "Warehouse name is required")
    private String name;
    @NotNull(message = "Warehouse location is required ")
    private String location;
}
