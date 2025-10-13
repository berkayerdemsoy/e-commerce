package com.example.shop_service.dto;

import com.example.shop_service.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductCreateDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private Long categoryId;
}
