package com.example.warehouse_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "shelves")
public class Shelf {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "shelf_code")
    private String shelfCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private Aisle aisle;

    private Long capacity;
    @Column(name = "used_capacity")
    private Long usedCapacity;

}
