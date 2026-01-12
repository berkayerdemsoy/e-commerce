package com.example.warehouse_service_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "warehouse_category")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WarehouseCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by")
    private Long assignedBy;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
