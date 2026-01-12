package com.example.warehouse_service_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_shelf")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductShelf {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "shelf_id")
    private Long shelfId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Builder.Default
    @Column(name = "quantity")
    private Integer quantity = 0;

    @Builder.Default
    @Column(name = "min_stock_level")
    private Integer minStockLevel = 10;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
