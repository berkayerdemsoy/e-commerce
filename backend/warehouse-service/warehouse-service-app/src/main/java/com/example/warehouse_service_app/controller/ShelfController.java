package com.example.warehouse_service_app.controller;

import com.example.warehouse_service_client.dto.ShelfDto;
import com.example.warehouse_service_app.service.ShelfService;
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
    public ResponseEntity<ShelfDto> getShelfById(@PathVariable("id") Long id){
        return ResponseEntity.ok(shelfService.getShelfById(id));
    }
    @GetMapping("/code/{code}")
    public ResponseEntity<ShelfDto> getShelfByCode(@PathVariable("code") String code){
        return ResponseEntity.ok(shelfService.getShelfByShelfCode(code));
    }
    @GetMapping("/all")
    public ResponseEntity<Page<ShelfDto>> getAllShelves(Pageable pageable){
        Page<ShelfDto> shelves = shelfService.getAllShelves(pageable);
        return ResponseEntity.ok(shelves);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelfById(@PathVariable("id") Long id){
        shelfService.deleteShelfById(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/create")
    public ResponseEntity<ShelfDto> createShelf(@Valid @RequestBody ShelfDto shelfDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(shelfService.createShelf( shelfDto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<ShelfDto> updateShelf(@PathVariable("id") Long id,@Valid @RequestBody ShelfDto shelfDto){
        return ResponseEntity.ok(shelfService.updateShelf(id,shelfDto));
    }
}
