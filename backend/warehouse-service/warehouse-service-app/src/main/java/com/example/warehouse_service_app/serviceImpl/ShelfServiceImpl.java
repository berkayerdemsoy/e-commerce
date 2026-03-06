package com.example.warehouse_service_app.serviceImpl;

import com.example.warehouse_service_client.dto.ShelfDto;
import com.example.warehouse_service_app.entity.Aisle;
import com.example.warehouse_service_app.entity.Shelf;
import com.example.warehouse_service_app.exception.AlreadyExistsException;
import com.example.warehouse_service_app.exception.NotFoundException;
import com.example.warehouse_service_app.mapper.ShelfMapper;
import com.example.warehouse_service_app.repository.AisleRepository;
import com.example.warehouse_service_app.repository.ShelfRepository;
import com.example.warehouse_service_app.service.ShelfService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShelfServiceImpl implements ShelfService {
    private final ShelfRepository shelfRepository;
    private final AisleRepository aisleRepository;
    private final ShelfMapper mapper;

    @Override
    public ShelfDto getShelfById(Long id) {
        Shelf shelf = shelfRepository.findById(id).orElseThrow(() -> new NotFoundException("Shelf not found"));
        return mapper.toDto(shelf);
    }

    @Override
    public ShelfDto getShelfByShelfCode(String code) {
        Shelf shelf = shelfRepository.findByShelfCodeIgnoreCase(code).orElseThrow(() -> new NotFoundException("Shelf not found"));
        return mapper.toDto(shelf);
    }

    @Override
    public Page<ShelfDto> getAllShelves(Pageable pageable) {
        Page<Shelf> shelves = shelfRepository.findAll(pageable);
        return shelves.map(mapper::toDto);
    }

    @Override
    public ShelfDto createShelf(ShelfDto shelfDto) {
        if (shelfRepository.findByShelfCodeIgnoreCase(shelfDto.getShelfCode()).isPresent()){
            throw new AlreadyExistsException("Shelf already exists");
        }
        Shelf shelf = mapper.toEntity(shelfDto);
        Aisle aisle = aisleRepository.findById(shelfDto.getAisleId()).orElseThrow(() -> new NotFoundException("Aisle not found"));
        shelf.setAisle(aisle);
        Shelf saved = shelfRepository.save(shelf);
        return mapper.toDto(saved);
    }

    @Override
    public ShelfDto updateShelf(Long id, ShelfDto shelfDto) {
        Shelf shelf = shelfRepository.findById(id).orElseThrow( () -> new NotFoundException("Shelf not found"));
        Aisle aisle = aisleRepository.findById(shelfDto.getAisleId()).orElseThrow(() -> new NotFoundException("Aisle not found"));
        mapper.toEntity(shelfDto);
        shelf.setAisle(aisle);
        Shelf updated = shelfRepository.save(shelf);
        return mapper.toDto(updated);
    }

    @Override
    public Void deleteShelfById(Long id) {
        shelfRepository.deleteById(id);
        return null;
    }

    @Override
    public List<ShelfDto> getShelvesByAisleId(Long aisleId) {
        List<Shelf> shelves = shelfRepository.findByAisleId(aisleId);
        return shelves.stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
