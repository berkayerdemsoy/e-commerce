package com.example.warehouse_service.repository;

import com.example.warehouse_service.entity.UserWarehouseAssignment;
import com.example.warehouse_service.entity.WarehouseRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseAssignmentRepository extends JpaRepository<UserWarehouseAssignment,Long> {
    List<UserWarehouseAssignment> findByUserId(Long userId);
    List<UserWarehouseAssignment> findByWarehouseIdAndRole(Long warehouseId, WarehouseRole role);
    boolean existsByUserIdAndWarehouseIdAndRole(Long userId,Long warehouseId,WarehouseRole role);
    Optional<UserWarehouseAssignment> findByUserIdAndWarehouseIdAndRole(Long userId,Long warehouseId,WarehouseRole role);

}
