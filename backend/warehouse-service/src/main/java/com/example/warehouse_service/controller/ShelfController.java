package com.example.warehouse_service.controller;

import com.example.warehouse_service.dto.ShelfDto;
import com.example.warehouse_service.service.ShelfService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shelf")
public class ShelfController {
    private final ShelfService shelfService;

    @GetMapping("/id/{id}")
    public ResponseEntity<ShelfDto> getShelfById(@PathVariable Long id){
        return ResponseEntity.ok(shelfService.getShelfById(id));
    }
    @GetMapping("/code/{code}")
    public ResponseEntity<ShelfDto> getShelfByCode(@PathVariable String code){
        return ResponseEntity.ok(shelfService.getShelfByShelfCode(code));
    }
    @GetMapping("/all")
    public ResponseEntity<Page<ShelfDto>> getAllShelves(Pageable pageable){
        Page<ShelfDto> shelves = shelfService.getAllShelves(pageable);
        return ResponseEntity.ok(shelves);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelfById(@PathVariable Long id){
        shelfService.deleteShelfById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/create")
    public ResponseEntity<ShelfDto> createShelf(@Valid @RequestBody ShelfDto shelfDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(shelfService.createShelf( shelfDto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ShelfDto> updateShelf(@PathVariable Long id,@Valid @RequestBody ShelfDto shelfDto){
        return ResponseEntity.ok(shelfService.updateShelf(id,shelfDto));
    }
}
