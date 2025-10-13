package com.example.warehouse_service.serviceImpl;

import com.example.warehouse_service.client.CategoryClient;
import com.example.warehouse_service.dto.AisleDto;
import com.example.warehouse_service.dto.CategoryDto;
import com.example.warehouse_service.entity.Aisle;
import com.example.warehouse_service.entity.Warehouse;
import com.example.warehouse_service.exception.AlreadyExistsException;
import com.example.warehouse_service.exception.NotFoundException;
import com.example.warehouse_service.mapper.AisleMapper;
import com.example.warehouse_service.repository.AisleRepository;
import com.example.warehouse_service.repository.WarehouseRepository;
import com.example.warehouse_service.service.AisleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AisleServiceImpl implements AisleService {

    private final AisleMapper aisleMapper;
    private final AisleRepository aisleRepository;
    private final WarehouseRepository warehouseRepository;
    private final CategoryClient categoryClient;

    @Override
    public AisleDto getAisleById(Long id) {
        Aisle aisle = aisleRepository.findById(id).orElseThrow(() -> new NotFoundException("Aisle not found"));
        return aisleMapper.toDto(aisle);
    }

    @Override
    public AisleDto getAisleByCode(String code) {
        Aisle aisle = aisleRepository.findByAisleCodeIgnoreCase(code).orElseThrow(() -> new NotFoundException("Aisle not found"));
        return aisleMapper.toDto(aisle);
    }

    @Override
    public Void deleteAisleById(Long id) {
        aisleRepository.deleteById(id);
        return null;
    }

    @Override
    public AisleDto createAisle(AisleDto aisleDto) {
        if (aisleRepository.findByAisleCodeIgnoreCase(aisleDto.getAisleCode()).isPresent()){
            throw new AlreadyExistsException("Aisle already exists");
        }
        Warehouse warehouse = warehouseRepository.findById(aisleDto.getWarehouseId()).orElseThrow(() -> new NotFoundException("Warehouse not found"));
        CategoryDto categoryDto = categoryClient.getCategoryById(aisleDto.getCategoryId());
        Aisle aisle = aisleMapper.toEntity(aisleDto);
        aisle.setWarehouse(warehouse);
        aisle.setCategoryId(categoryDto.getId());
        Aisle saved = aisleRepository.save(aisle);
        return aisleMapper.toDto(saved);

    }

    @Override
    public AisleDto updateAisle(Long id, AisleDto aisleDto) {
        Aisle aisle = aisleRepository.findById(id).orElseThrow(() -> new NotFoundException("Aisle not found"));
        Warehouse warehouse = warehouseRepository.findById(aisleDto.getWarehouseId()).orElseThrow(() -> new NotFoundException("Warehouse not found"));
        CategoryDto categoryDto = categoryClient.getCategoryById(aisleDto.getCategoryId());
        aisleMapper.toEntity(aisleDto);
        aisle.setWarehouse(warehouse);
        aisle.setCategoryId(categoryDto.getId());
        Aisle updated = aisleRepository.save(aisle);
        return aisleMapper.toDto(updated);

    }

    @Override
    public Page<AisleDto> getAllAisles(Pageable pageable) {
        Page<Aisle> aisles = aisleRepository.findAll(pageable);
        return aisles.map(aisleMapper::toDto);
    }

}
