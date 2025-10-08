package com.example.shop_service.mapper;

import com.example.shop_service.dto.ProductCreateDto;
import com.example.shop_service.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductCreateDto toDto(Product product);
    Product toEntity(ProductCreateDto dto);
}
