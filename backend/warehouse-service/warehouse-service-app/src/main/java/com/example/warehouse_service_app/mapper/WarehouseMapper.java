package com.example.warehouse_service_app.mapper;

import com.example.warehouse_service_client.dto.WarehouseDto;
import com.example.warehouse_service_app.entity.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface WarehouseMapper
{
    WarehouseDto toDto(Warehouse warehouse);
    Warehouse toEntity(WarehouseDto warehouseDto);
}
