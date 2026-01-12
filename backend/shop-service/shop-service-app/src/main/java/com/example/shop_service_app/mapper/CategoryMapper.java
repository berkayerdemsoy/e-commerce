package com.example.shop_service_app.mapper;

import com.example.shop_service_client.dto.CategoryDto;
import com.example.shop_service_app.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);
}
