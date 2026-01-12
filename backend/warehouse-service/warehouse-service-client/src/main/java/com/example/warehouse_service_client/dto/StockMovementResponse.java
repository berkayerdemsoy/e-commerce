package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovementResponse {
    private Long id;
    private Long productId;
    private Long shelfId;
    private String movementType;
    private Integer previousQuantity;
    private Integer newQuantity;
    private String reason;
    private String createdAt; // ISO formatted string via mapper
}
