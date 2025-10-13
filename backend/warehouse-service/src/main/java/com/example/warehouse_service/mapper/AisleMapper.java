package com.example.warehouse_service.mapper;

import com.example.warehouse_service.dto.AisleDto;
import com.example.warehouse_service.entity.Aisle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AisleMapper {
    @Mapping(source = "warehouse.id",target = "warehouseId")
    AisleDto toDto(Aisle aisle);
    @Mapping(source = "warehouseId",target = "warehouse.id")
    Aisle toEntity(AisleDto aisleDto);
}
