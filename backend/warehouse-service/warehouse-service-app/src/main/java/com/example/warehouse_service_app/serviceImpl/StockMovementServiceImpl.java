package com.example.warehouse_service_app.serviceImpl;

import com.example.shop_service_client.client.ProductClient;
import com.example.warehouse_service_app.entity.ProductShelf;
import com.example.warehouse_service_app.entity.Shelf;
import com.example.warehouse_service_app.entity.StockMovement;
import com.example.warehouse_service_app.exception.InvalidStockChangeException;
import com.example.warehouse_service_app.exception.NotFoundException;
import com.example.warehouse_service_app.mapper.StockMovementMapper;
import com.example.warehouse_service_app.repository.ProductShelfRepository;
import com.example.warehouse_service_app.repository.ShelfRepository;
import com.example.warehouse_service_app.repository.StockMovementRepository;
import com.example.warehouse_service_app.service.StockMovementService;
import com.example.warehouse_service_client.dto.StockMovementRequest;
import com.example.warehouse_service_client.dto.StockMovementResponse;
import com.example.warehouse_service_client.enums.MovementType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductClient productClient;
    private final ShelfRepository shelfRepository;
    private final ProductShelfRepository productShelfRepository;
    private final StockMovementMapper mapper;

    @Override
    @Transactional
    public StockMovementResponse addStockMovement(StockMovementRequest req) {
        productClient.getProductById(req.getProductId());

        Shelf shelf = shelfRepository.findById(req.getShelfId())
                .orElseThrow(() -> new NotFoundException("Shelf not found"));

        ProductShelf productShelf = productShelfRepository
                .findByProductIdAndShelfId(req.getProductId(), req.getShelfId())
                .orElseGet(() -> {
                    ProductShelf ps = ProductShelf.builder()
                            .productId(req.getProductId())
                            .shelfId(req.getShelfId())
                            .warehouseId(shelf.getAisle().getWarehouse().getId())
                            .quantity(0)
                            .minStockLevel(10)
                            .build();
                    return productShelfRepository.save(ps);
                });

        Integer previous = productShelf.getQuantity() == null ? 0 : productShelf.getQuantity();
        Integer newQty = req.getNewQuantity();
        if (newQty == null || newQty < 0) {
            throw new InvalidStockChangeException("New quantity must be non negative");
        }
        if (productShelf.getMaxCapacity() != null && newQty > productShelf.getMaxCapacity()) {
            throw new InvalidStockChangeException("Exceeds max capacity");
        }

        productShelf.setQuantity(newQty);
        ProductShelf savedShelf = productShelfRepository.save(productShelf);

        MovementType type = determineMovementType(previous, newQty, req.getReason());

        StockMovement movement = StockMovement.builder()
                .productId(req.getProductId())
                .shelfId(req.getShelfId())
                .movementType(type)
                .previousQuantity(previous)
                .newQuantity(newQty)
                .reason(req.getReason())
                .createdAt(LocalDateTime.now())
                .build();

        StockMovement savedMovement = stockMovementRepository.save(movement);
        return mapper.toResponse(savedMovement);
    }

    private MovementType determineMovementType(Integer previous, Integer now, String reason) {
        if (now > previous) return MovementType.INBOUND;
        if (now < previous) return MovementType.OUTBOUND;
        return MovementType.ADJUSTMENT;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockMovementResponse> getAllStockMovements(Long productId, Pageable pageable) {
        Page<StockMovement> page = stockMovementRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable);
        return page.map(mapper::toResponse);
    }
}
