package com.example.warehouse_service_app.serviceImpl;

import com.example.shop_service_client.client.CategoryClient;
import com.example.shop_service_client.dto.CategoryDto;
import com.example.warehouse_service_app.entity.WarehouseCategory;
import com.example.warehouse_service_app.exception.AlreadyExistsException;
import com.example.warehouse_service_app.exception.NotFoundException;
import com.example.warehouse_service_app.mapper.WarehouseCategoryMapper;
import com.example.warehouse_service_app.repository.WarehouseCategoryRepository;
import com.example.warehouse_service_app.service.WarehouseCategoryService;
import com.example.warehouse_service_client.dto.WarehouseCategoryDto;
import com.example.warehouse_service_client.dto.WarehouseCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseCategoryServiceImpl implements WarehouseCategoryService {
    private final WarehouseCategoryRepository  warehouseCategoryRepository;
    private final WarehouseCategoryMapper mapper;
    private final CategoryClient categoryClient;

    @Override
    public WarehouseCategoryDto assignCategoryToWarehouse(WarehouseCategoryRequest warehouseCategoryRequest) {
        categoryClient.getCategoryById(warehouseCategoryRequest.getCategoryId());
        if(warehouseCategoryRepository.existsByWarehouseIdAndCategoryIdAndIsActiveTrue(warehouseCategoryRequest.getWarehouseId(), warehouseCategoryRequest.getCategoryId())){
            throw new AlreadyExistsException("Category already assigned to warehouse");
        }
        if (warehouseCategoryRepository.existsByWarehouseIdAndCategoryIdAndIsActiveFalse(warehouseCategoryRequest.getWarehouseId(), warehouseCategoryRequest.getCategoryId())){
            WarehouseCategory wc = warehouseCategoryRepository.findByWarehouseIdAndCategoryId(warehouseCategoryRequest.getWarehouseId(), warehouseCategoryRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Warehouse Category not found"));
            wc.setIsActive(true);
            WarehouseCategory activated = warehouseCategoryRepository.save(wc);
            return mapper.toDto(activated);
        }
        WarehouseCategory warehouseCategory = WarehouseCategory.builder()
                .warehouseId(warehouseCategoryRequest.getWarehouseId())
                .categoryId(warehouseCategoryRequest.getCategoryId())
                .assignedAt(LocalDateTime.now())
                .assignedBy(warehouseCategoryRequest.getAssignedBy())
                .isActive(true)
                .build();
        WarehouseCategory saved = warehouseCategoryRepository.save(warehouseCategory);
        return mapper.toDto(saved);

    }

    @Override
    public WarehouseCategoryDto removeCategoryAssignFromWarehouse(Long categoryId, Long warehouseId) {
        WarehouseCategory wc = warehouseCategoryRepository
                .findByWarehouseIdAndCategoryId(warehouseId,categoryId)
                .orElseThrow(() -> new NotFoundException("Assignment Not Found"));

        wc.setIsActive(false);
        WarehouseCategory removed = warehouseCategoryRepository.save(wc);
        return mapper.toDto(removed);
    }

    @Override
    public List<WarehouseCategoryDto> getCategoriesByWarehouse(Long warehouseId) {
        List<WarehouseCategory> assignments = warehouseCategoryRepository
                .findByWarehouseIdAndIsActiveTrue(warehouseId);

        if (assignments.isEmpty()) {
            return List.of();
        }

        return assignments.stream()
                .map(mapper::toDto) // WarehouseCategory → WarehouseCategoryDto
                .toList();
    }

}
