package com.example.warehouse_service_app.entity;

import com.example.warehouse_service_client.enums.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name ="stock_movement")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockMovement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "shelf_id")
    private Long shelfId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type")
    private MovementType movementType;


    @Column(name = "previous_quantity")
    private Integer previousQuantity;


    @Column(name = "new_quantity")
    private Integer newQuantity;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
