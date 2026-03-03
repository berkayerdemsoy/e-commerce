package com.example.warehouse_service_app.serviceImpl;

import com.example.warehouse_service_client.dto.WarehouseDto;
import com.example.warehouse_service_client.dto.WarehouseSummaryDto;
import com.example.warehouse_service_app.entity.Warehouse;
import com.example.warehouse_service_app.exception.AlreadyExistsException;
import com.example.warehouse_service_app.exception.NotFoundException;
import com.example.warehouse_service_app.mapper.WarehouseMapper;
import com.example.warehouse_service_app.repository.AisleRepository;
import com.example.warehouse_service_app.repository.ProductShelfRepository;
import com.example.warehouse_service_app.repository.ShelfRepository;
import com.example.warehouse_service_app.repository.WarehouseRepository;
import com.example.warehouse_service_app.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final AisleRepository aisleRepository;
    private final ShelfRepository shelfRepository;
    private final ProductShelfRepository productShelfRepository;

    @Override
    public Page<WarehouseDto> getAllWarehouses(Pageable pageable) {
        Page<Warehouse> warehouses = warehouseRepository.findAll(pageable);
        return warehouses.map(warehouseMapper::toDto);
    }

    @Override
    public WarehouseDto getWarehouseById(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new NotFoundException("Warehouse not found"));
        return warehouseMapper.toDto(warehouse);
    }

    @Override
    public WarehouseDto getWarehouseByName(String name) {
        Warehouse warehouse = warehouseRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("Warehouse not found"));
        return warehouseMapper.toDto(warehouse);
    }

    @Override
    public WarehouseDto createWarehouse(WarehouseDto warehouseDto) {
        if (warehouseRepository.findByNameIgnoreCase(warehouseDto.getName()).isPresent()) {
            throw new AlreadyExistsException("Warehouse Already exists");
        }
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDto);
        Warehouse saved = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(saved);
    }

    @Override
    public WarehouseDto updateWarehouse(Long id, WarehouseDto warehouseDto) {
        Warehouse warehouse = warehouseRepository.findById(id).orElseThrow(() -> new NotFoundException("Warehouse not found"));
        warehouse.setLocation(warehouseDto.getLocation());
        warehouse.setName(warehouseDto.getName());
        Warehouse saved = warehouseRepository.save(warehouse);
        return warehouseMapper.toDto(saved);
    }

    @Override
    public Void deleteWarehouse(Long id) {
        warehouseRepository.deleteById(id);
        return null;
    }

    @Override
    public WarehouseSummaryDto getWarehouseSummary(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));

        long totalAisles = aisleRepository.countByWarehouseId(id);
        long totalShelves = shelfRepository.countByWarehouseId(id);
        long totalProducts = productShelfRepository.countDistinctProductsByWarehouseId(id);
        long totalStock = productShelfRepository.sumQuantityByWarehouseId(id);
        long lowStockCount = productShelfRepository.countLowStockByWarehouseId(id);

        return WarehouseSummaryDto.builder()
                .warehouseId(warehouse.getId())
                .warehouseName(warehouse.getName())
                .location(warehouse.getLocation())
                .totalAisles(totalAisles)
                .totalShelves(totalShelves)
                .totalProducts(totalProducts)
                .totalStockQuantity(totalStock)
                .lowStockAlertCount(lowStockCount)
                .build();
    }
}
