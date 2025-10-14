package com.example.warehouse_service.controller;

import com.example.warehouse_service.entity.UserWarehouseAssignment;
import com.example.warehouse_service.entity.WarehouseRole;
import com.example.warehouse_service.service.WarehouseAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/assignments")
public class WarehouseAssignmentController {
    private final WarehouseAssignmentService warehouseAssignmentService;

    @PostMapping("/admin")
    public ResponseEntity<Void> assignWarehouseAdmin(@RequestBody UserWarehouseAssignment assignment){
        warehouseAssignmentService.assignWarehouseAdmin(assignment.getUserId(), assignment.getWarehouseId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/manager")
    public ResponseEntity<Void> assignWarehouseManager(@RequestBody UserWarehouseAssignment assignment){
        warehouseAssignmentService.assignWarehouseManager(assignment.getWarehouseId(), assignment.getUserId());
        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{warehouseId}/user/{userId}/role/{role}")
    public ResponseEntity<Void> removeAssignment(
            @PathVariable Long warehouseId,
            @PathVariable Long userId,
            @PathVariable WarehouseRole role
            ){
        warehouseAssignmentService.removeAssignment(warehouseId,userId ,role);
        return ResponseEntity.ok().build();
    }
}
