package com.example.warehouse_service.client;

import com.example.warehouse_service.config.FeignConfig;
import com.example.warehouse_service.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "categoryClient", url = "http://api-gateway:8080",configuration = FeignConfig.class)
public interface CategoryClient {

    @GetMapping("/api/category/id/{id}")
    CategoryDto getCategoryById(@PathVariable Long id);
}


