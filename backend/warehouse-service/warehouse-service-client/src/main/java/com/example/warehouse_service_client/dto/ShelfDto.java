package com.example.warehouse_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShelfDto {
    private Long id;
    private String shelfCode;
    private Long aisleId;
    private Long capacity;
    private Long usedCapacity;
}
