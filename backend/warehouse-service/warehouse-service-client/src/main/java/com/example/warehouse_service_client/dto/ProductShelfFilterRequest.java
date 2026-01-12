package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductShelfFilterRequest {
    private Long warehouseId;
    private Long shelfId;
    private Long productId;
    private Integer initialQuantity;
    private Integer minQuantity;
    private Integer maxQuantity;
}
