package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementRequest {
    private Long productId;
    private Long shelfId;
    private Integer newQuantity;
    private String reason;
}
