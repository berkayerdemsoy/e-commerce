package com.example.shop_service_app.mapper;

import com.example.shop_service_client.dto.ProductCreateDto;
import com.example.shop_service_app.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id",target = "categoryId")
    ProductCreateDto toDto(Product product);
    Product toEntity(ProductCreateDto dto);
}
