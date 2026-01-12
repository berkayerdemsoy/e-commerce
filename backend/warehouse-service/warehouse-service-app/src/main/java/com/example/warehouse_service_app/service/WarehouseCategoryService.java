package com.example.warehouse_service_app.service;


import com.example.shop_service_client.dto.CategoryDto;
import com.example.warehouse_service_client.dto.WarehouseCategoryDto;
import com.example.warehouse_service_client.dto.WarehouseCategoryRequest;

import java.util.List;

public interface WarehouseCategoryService {
    WarehouseCategoryDto assignCategoryToWarehouse(WarehouseCategoryRequest warehouseCategoryRequest);
    WarehouseCategoryDto removeCategoryAssignFromWarehouse(Long categoryId,Long warehouseId);
    List<WarehouseCategoryDto> getCategoriesByWarehouse(Long warehouseId);
}
