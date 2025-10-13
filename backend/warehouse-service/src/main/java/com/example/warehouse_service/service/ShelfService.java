package com.example.warehouse_service.service;

import com.example.warehouse_service.dto.ShelfDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShelfService {
    ShelfDto getShelfById(Long id);
    ShelfDto getShelfByShelfCode(String code);
    Page<ShelfDto> getAllShelves(Pageable pageable);
    ShelfDto createShelf(ShelfDto shelfDto);
    ShelfDto updateShelf(Long id , ShelfDto shelfDto);
    Void deleteShelfById(Long id);
}
