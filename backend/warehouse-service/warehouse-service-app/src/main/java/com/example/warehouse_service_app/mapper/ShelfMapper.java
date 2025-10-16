package com.example.warehouse_service_app.mapper;

import com.example.warehouse_service_client.dto.ShelfDto;
import com.example.warehouse_service_app.entity.Shelf;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShelfMapper {
    @Mapping(source = "aisle.id",target = "aisleId")
    ShelfDto toDto(Shelf shelf);
    @Mapping(source = "aisleId",target = "aisle.id")
    Shelf toEntity(ShelfDto shelfDto);
}
