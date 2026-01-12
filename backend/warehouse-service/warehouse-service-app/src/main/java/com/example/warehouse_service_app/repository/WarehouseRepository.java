package com.example.warehouse_service_app.repository;

import com.example.warehouse_service_app.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {
    Optional<Warehouse> findByNameIgnoreCase(String name);
}
