package com.example.shop_service.mapper;

import com.example.shop_service.dto.CategoryDto;
import com.example.shop_service.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto categoryDto);
}
