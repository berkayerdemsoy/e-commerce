package com.example.warehouse_service_app.mapper;

import com.example.warehouse_service_app.entity.WarehouseCategory;
import com.example.warehouse_service_client.dto.WarehouseCategoryDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WarehouseCategoryMapper {
    WarehouseCategoryDto toDto(WarehouseCategory warehouseCategory);
}
