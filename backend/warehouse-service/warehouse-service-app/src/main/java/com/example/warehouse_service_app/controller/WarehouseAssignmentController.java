package com.example.warehouse_service_app.controller;

import com.example.warehouse_service_app.entity.UserWarehouseAssignment;
import com.example.warehouse_service_client.dto.WarehouseAssignmentDto;
import com.example.warehouse_service_client.dto.WarehouseAssignmentRequest;
import com.example.warehouse_service_client.enums.WarehouseRole;
import com.example.warehouse_service_app.service.WarehouseAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class WarehouseAssignmentController {
    private final WarehouseAssignmentService warehouseAssignmentService;

    @PostMapping("/admin")
    public ResponseEntity<WarehouseAssignmentDto> assignWarehouseAdmin(
            @RequestBody WarehouseAssignmentRequest request) {
        WarehouseAssignmentDto dto =
                warehouseAssignmentService.assignWarehouseAdmin(request.getUserId(), request.getWarehouseId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/manager")
    public ResponseEntity<WarehouseAssignmentDto> assignWarehouseManager(
            @RequestBody WarehouseAssignmentRequest request) {
        WarehouseAssignmentDto dto =
                warehouseAssignmentService.assignWarehouseManager(request.getWarehouseId(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{warehouseId}/user/{userId}/role/{role}")
    public ResponseEntity<Void> removeAssignment(
            @PathVariable Long warehouseId,
            @PathVariable Long userId,
            @PathVariable WarehouseRole role) {
        warehouseAssignmentService.removeAssignment(warehouseId, userId, role);
        return ResponseEntity.noContent().build();
    }
}
