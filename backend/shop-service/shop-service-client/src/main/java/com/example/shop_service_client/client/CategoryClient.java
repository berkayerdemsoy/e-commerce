package com.example.shop_service_client.client;


import com.example.shop_service_client.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "shop-service",
        contextId = "categoryClient",
        path = "/shop/category",
        url = "http://api-gateway:8080")
public interface CategoryClient {

    @GetMapping("/id/{id}")
    CategoryDto getCategoryById(@PathVariable("id") Long id);

    @GetMapping("/name/{name}")
    CategoryDto getCategoryByName(@PathVariable("name") String name);

    @GetMapping("/all")
    Page<CategoryDto> getAllCategories(Pageable pageable);

    @PostMapping("/create")
    CategoryDto createCategory(@RequestBody CategoryDto categoryDto);

    @PutMapping("/{id}")
    CategoryDto updateCategory(@PathVariable("id") Long id,
                               @RequestBody CategoryDto categoryDto);

    @DeleteMapping("/{id}")
    void deleteCategory(@PathVariable("id") Long id);
}
