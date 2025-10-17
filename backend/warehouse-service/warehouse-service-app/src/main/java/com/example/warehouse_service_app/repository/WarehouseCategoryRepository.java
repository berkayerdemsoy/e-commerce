package com.example.warehouse_service_app.repository;

import com.example.warehouse_service_app.entity.WarehouseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseCategoryRepository extends JpaRepository<WarehouseCategory,Long> {
    boolean existsByWarehouseIdAndCategoryIdAndIsActiveTrue(Long warehouseId,Long categoryId);
    List<WarehouseCategory> findByWarehouseIdAndIsActiveTrue(Long warehouseId);
    Optional<WarehouseCategory> findByWarehouseIdAndCategoryId(Long warehouseId,Long categoryId);

}
