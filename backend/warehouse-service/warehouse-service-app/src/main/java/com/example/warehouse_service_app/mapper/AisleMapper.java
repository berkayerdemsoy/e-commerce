package com.example.warehouse_service_app.mapper;

import com.example.warehouse_service_client.dto.AisleDto;
import com.example.warehouse_service_app.entity.Aisle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AisleMapper {
    @Mapping(source = "warehouse.id",target = "warehouseId")
    AisleDto toDto(Aisle aisle);
    @Mapping(source = "warehouseId",target = "warehouse.id")
    Aisle toEntity(AisleDto aisleDto);
}
