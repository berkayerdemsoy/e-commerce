package com.example.warehouse_service.mapper;

import com.example.warehouse_service.dto.WarehouseDto;
import com.example.warehouse_service.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WarehouseMapper
{
    WarehouseDto toDto(Warehouse warehouse);
    Warehouse toEntity(WarehouseDto warehouseDto);
}
