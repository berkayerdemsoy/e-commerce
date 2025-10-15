package com.example.warehouse_service.serviceImpl;

import com.example.user_service_client.client.UserServiceClient;
import com.example.warehouse_service.entity.UserWarehouseAssignment;
import com.example.warehouse_service.entity.WarehouseRole;
import com.example.warehouse_service.exception.AlreadyExistsException;
import com.example.warehouse_service.exception.NotFoundException;
import com.example.warehouse_service.repository.WarehouseAssignmentRepository;
import com.example.warehouse_service.repository.WarehouseRepository;
import com.example.warehouse_service.service.WarehouseAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseAssignmentServiceImpl implements WarehouseAssignmentService {

    private final WarehouseAssignmentRepository assignmentRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserServiceClient userClient;
    @Override
    public void assignWarehouseAdmin(Long userId, Long warehouseId) {
    warehouseRepository.findById(warehouseId).orElseThrow(() -> new NotFoundException("Warehouse not found"));

    userClient.getUserById(userId);
    if(assignmentRepository.existsByUserIdAndWarehouseIdAndRole(userId,warehouseId,WarehouseRole.WAREHOUSE_ADMIN)){
        throw new AlreadyExistsException("User already assigned as Warehouse Admin");
    }

        UserWarehouseAssignment assignment = UserWarehouseAssignment.builder()
                .userId(userId)
                .assignedAt(LocalDateTime.now())
                .warehouseId(warehouseId)
                .role(WarehouseRole.WAREHOUSE_ADMIN)
                .build();

        assignmentRepository.save(assignment);
    }

    @Override
    public void assignWarehouseManager(Long warehouseId, Long userId) {
        warehouseRepository.findById(warehouseId).orElseThrow(() -> new NotFoundException("Warehouse not found"));
        userClient.getUserById(userId);
        if(assignmentRepository.existsByUserIdAndWarehouseIdAndRole(userId,warehouseId,WarehouseRole.WAREHOUSE_MANAGER)){
            throw new AlreadyExistsException("User already assigned as Warehouse Manager");
        }

        UserWarehouseAssignment assignment = UserWarehouseAssignment.builder()
                .userId(userId)
                .warehouseId(warehouseId)
                .assignedAt(LocalDateTime.now())
                .role(WarehouseRole.WAREHOUSE_MANAGER)
                .build();

        assignmentRepository.save(assignment);
    }

    @Override
    public void removeAssignment(Long warehouseId, Long userId, WarehouseRole role) {
        UserWarehouseAssignment assignment = assignmentRepository
                .findByUserIdAndWarehouseIdAndRole(userId,warehouseId,role)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        assignmentRepository.delete(assignment);
    }

    @Override
    public List<Long> getWarehouseIdsByUser(Long userId) {
        return assignmentRepository.findByUserId(userId)
                .stream()
                .map(UserWarehouseAssignment::getWarehouseId)
                .toList();
    }
}
