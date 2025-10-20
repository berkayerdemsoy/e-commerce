package com.example.warehouse_service_app.repository;

import com.example.warehouse_service_app.entity.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface StockMovementRepository extends JpaRepository<StockMovement , Long> {
    Page<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);
    Page<StockMovement> findByCreatedAtBetween(LocalDateTime start , LocalDateTime end, Pageable pageable);

}
