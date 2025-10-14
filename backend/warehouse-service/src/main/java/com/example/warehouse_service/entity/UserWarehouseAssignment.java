package com.example.warehouse_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@Table(name = "user_warehouse")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWarehouseAssignment {
    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(name = "warehouse_id",nullable = false)
    private Long warehouseId;
    @Column(name = "user_id",nullable = false)
    private Long userId;
    private WarehouseRole role;
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
//    @Column(name = "is_active")
//    private Boolean isActive;
}
