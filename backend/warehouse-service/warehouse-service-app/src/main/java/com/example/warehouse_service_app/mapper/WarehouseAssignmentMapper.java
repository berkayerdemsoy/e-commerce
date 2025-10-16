package com.example.warehouse_service_app.mapper;

import com.example.warehouse_service_app.entity.UserWarehouseAssignment;
import com.example.warehouse_service_client.dto.WarehouseAssignmentDto;
import com.example.warehouse_service_client.dto.WarehouseAssignmentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WarehouseAssignmentMapper {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "warehouseId", target = "warehouseId")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "assignedAt", target = "assignedAt")
    WarehouseAssignmentDto toDto(UserWarehouseAssignment entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "warehouseId", target = "warehouseId")
    @Mapping(source = "role", target = "role")
    @Mapping(target = "assignedAt", ignore = true)
    UserWarehouseAssignment toEntity(WarehouseAssignmentRequest request);
}
