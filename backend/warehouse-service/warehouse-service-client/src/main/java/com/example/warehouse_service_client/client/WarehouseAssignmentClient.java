package com.example.warehouse_service_client.client;

import com.example.warehouse_service_client.dto.WarehouseAssignmentRequest;
import com.example.warehouse_service_client.enums.WarehouseRole;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "warehouse-service",
        contextId = "warehouseAssignmentClient",
        path = "/api/assignments",
        url = "http://api-gateway:8080"
)
public interface WarehouseAssignmentClient {

    @PostMapping("/admin")
    void assignWarehouseAdmin(@RequestBody WarehouseAssignmentRequest assignment);

    @PostMapping("/manager")
    void assignWarehouseManager(@RequestBody WarehouseAssignmentRequest assignment);

    @DeleteMapping("/{warehouseId}/user/{userId}/role/{role}")
    void removeAssignment(
            @PathVariable("warehouseId") Long warehouseId,
            @PathVariable("userId") Long userId,
            @PathVariable("role") WarehouseRole role
    );
}
