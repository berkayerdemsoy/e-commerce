package com.example.warehouse_service_app.service;

import com.example.warehouse_service_client.dto.AisleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AisleService {
    AisleDto getAisleById(Long id);
    AisleDto getAisleByCode(String code);
    Void deleteAisleById(Long id);
    AisleDto createAisle(AisleDto aisleDto);
    AisleDto updateAisle(Long id,AisleDto aisleDto);
    Page<AisleDto> getAllAisles(Pageable pageable);
    List<AisleDto> getAislesByWarehouseId(Long warehouseId);
}
