package com.example.shop_service_client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long categoryId;
}
