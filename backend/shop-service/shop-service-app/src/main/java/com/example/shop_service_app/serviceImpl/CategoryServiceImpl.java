package com.example.shop_service_app.serviceImpl;

import com.example.shop_service_client.dto.CategoryDto;
import com.example.shop_service_app.entity.Category;
import com.example.shop_service_app.exception.AlreadyExistsException;
import com.example.shop_service_app.exception.NotFoundException;
import com.example.shop_service_app.mapper.CategoryMapper;
import com.example.shop_service_app.repository.CategoryRepository;
import com.example.shop_service_app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public Void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
        return null;
    }

    @Override
    public CategoryDto getCategoryByName(String name) {
        Category category = categoryRepository.findByNameIgnoreCase(name).orElseThrow(() ->
                new NotFoundException("Category not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepository.findByNameIgnoreCase(categoryDto.name).isPresent()){
            throw new AlreadyExistsException("Category is already exists");
        }
        Category category = Category.builder()
                .name(categoryDto.name)
                .build();
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Category not found"));
        category.setName(categoryDto.name);
        Category saved = categoryRepository.save(category);
        return categoryMapper.toDto(saved);
    }

    @Override
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toDto);
    }
    @Override
    public List<CategoryDto> getCategoriesByIds(List<Long> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        if (categories.isEmpty()) {
            throw new NotFoundException("No categories found for given ids");
        }
        return categories.stream()
                .map(categoryMapper::toDto)
                .toList();
    }



}
