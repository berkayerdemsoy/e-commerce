package com.example.shipment_service.repository;

import com.example.shipment_service.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
    Page<Shipment> findByUserId(Long id, Pageable pageable);
}
