package com.example.warehouse_service_app.repository;

import com.example.warehouse_service_app.entity.Aisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AisleRepository extends JpaRepository<Aisle,Long> {
    Optional<Aisle> findByAisleCode(String code);
    Optional<Aisle> findByAisleCodeIgnoreCase(String code);
}
