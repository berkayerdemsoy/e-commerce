package com.example.warehouse_service_client.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDto {
    private Long id;
    @NotNull(message = "Warehouse name is required")
    private String name;
    @NotNull(message = "Warehouse location is required ")
    private String location;
}
