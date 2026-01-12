package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductShelfResponse {
    private Long productId;
    private Long shelfId;
    private int quantity;
    private int minStockLevel;
}

