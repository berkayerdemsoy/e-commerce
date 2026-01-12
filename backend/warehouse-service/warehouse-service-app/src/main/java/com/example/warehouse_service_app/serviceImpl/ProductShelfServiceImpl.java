package com.example.warehouse_service_app.serviceImpl;

import com.example.shop_service_client.client.ProductClient;
import com.example.warehouse_service_app.entity.ProductShelf;
import com.example.warehouse_service_app.entity.Shelf;
import com.example.warehouse_service_app.exception.AlreadyExistsException;
import com.example.warehouse_service_app.exception.NotFoundException;
import com.example.warehouse_service_app.mapper.ProductShelfMapper;
import com.example.warehouse_service_app.repository.ProductShelfRepository;
import com.example.warehouse_service_app.repository.ShelfRepository;
import com.example.warehouse_service_app.service.ProductShelfService;
import com.example.warehouse_service_client.dto.ProductShelfFilterRequest;
import com.example.warehouse_service_client.dto.ProductShelfResponse;
import com.example.warehouse_service_client.dto.ProductShelfUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductShelfServiceImpl implements ProductShelfService  {

    private final ProductShelfRepository productShelfRepository;
    private final ProductClient productClient;
    private final ShelfRepository shelfRepository;
    private final ProductShelfMapper mapper;
    @Override
    public ProductShelfResponse assignProductToWarehouse(ProductShelfFilterRequest dto) {
        if(productShelfRepository.findByProductIdAndShelfId(dto.getProductId(), dto.getShelfId()).isPresent()){
            throw new AlreadyExistsException("Product already assigned");
        }
        productClient.getProductById(dto.getProductId());
        Shelf shelf = shelfRepository.findById(dto.getShelfId()).orElseThrow(
                () -> new NotFoundException("Shelf not found")
        );

        ProductShelf productShelf = ProductShelf.builder()
                .productId(dto.getProductId())
                .shelfId(dto.getShelfId())
                .warehouseId(shelf.getAisle().getWarehouse().getId())
                .quantity(dto.getInitialQuantity())
                .minStockLevel(10)
                .updatedAt(LocalDateTime.now())
                .build();

        ProductShelf saved = productShelfRepository.save(productShelf);
        return mapper.toDto(saved);
    }

    @Override
    public ProductShelfResponse updateStock(ProductShelfUpdateRequest request) {
        ProductShelf shelf = productShelfRepository.findByProductIdAndShelfId(request.getProductId(), request.getShelfId())
                .orElseThrow(() -> new NotFoundException("Product Shelf not found"));

        shelf.setUpdatedAt(LocalDateTime.now());
        shelf.setQuantity(request.getNewQuantity());
        ProductShelf saved = productShelfRepository.save( shelf);
        return mapper.toDto(saved);
    }

    @Override
    public Page<ProductShelfResponse> getLowStockAlerts(Long warehouseId, Pageable pageable) {
        Page<ProductShelf> productShelves = productShelfRepository.findLowStockByWarehouse(warehouseId,pageable);
        return productShelves.map(mapper::toDto);
    }
}
