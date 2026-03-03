package com.example.warehouse_service_app.repository;

import com.example.warehouse_service_app.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf,Long> {
    Optional<Shelf> findByShelfCodeIgnoreCase(String code);

    @Query("SELECT COUNT(s) FROM Shelf s WHERE s.aisle.warehouse.id = :warehouseId")
    long countByWarehouseId(@Param("warehouseId") Long warehouseId);
}
