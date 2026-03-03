package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dashboard özet bilgisi için DTO.
 * Bir warehouse'un genel stok durumunu özetler.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseSummaryDto {
    private Long warehouseId;
    private String warehouseName;
    private String location;
    private long totalProducts;
    private long totalAisles;
    private long totalShelves;
    private long lowStockAlertCount;
    private long totalStockQuantity;
}

