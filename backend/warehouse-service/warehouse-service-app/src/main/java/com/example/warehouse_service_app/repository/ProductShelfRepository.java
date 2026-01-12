package com.example.warehouse_service_app.repository;

import com.example.warehouse_service_app.entity.ProductShelf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductShelfRepository extends JpaRepository<ProductShelf,Long> {
    Optional<ProductShelf> findByProductIdAndShelfId(Long productId,Long shelfId);

    Page<ProductShelf> findByShelfId(Long shelfId, Pageable pageable);
    Page<ProductShelf> findByWarehouseId(Long warehouseId,Pageable pageable);

    @Query("SELECT ps FROM ProductShelf ps WHERE ps.warehouseId = :warehouseId " +
    "AND ps.quantity < ps.minStockLevel")
    Page<ProductShelf> findLowStockByWarehouse(@Param("warehouseId") Long warehouseId,Pageable pageable);
}
