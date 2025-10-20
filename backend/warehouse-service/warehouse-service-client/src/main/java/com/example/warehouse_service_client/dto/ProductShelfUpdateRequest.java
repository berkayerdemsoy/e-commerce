package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductShelfUpdateRequest {
    private Long shelfId;
    private Long productId;
    private Integer newQuantity;
}
