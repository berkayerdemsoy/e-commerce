package com.example.shop_service_app.service;


import com.example.shop_service_client.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto getCategoryById(Long id);
    Void deleteCategoryById(Long id);
    CategoryDto getCategoryByName(String name);
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategory(Long id,CategoryDto categoryDto);
    Page<CategoryDto> getAllCategories(Pageable pageable);

}
