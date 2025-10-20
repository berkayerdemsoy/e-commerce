package com.example.warehouse_service_app.mapper;

import com.example.warehouse_service_app.entity.ProductShelf;
import com.example.warehouse_service_client.dto.ProductShelfResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductShelfMapper {
    ProductShelfResponse toDto(ProductShelf productShelf);
}
