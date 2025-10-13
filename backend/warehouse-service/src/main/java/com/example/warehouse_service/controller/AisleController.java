package com.example.warehouse_service.controller;

import com.example.warehouse_service.dto.AisleDto;
import com.example.warehouse_service.service.AisleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aisles")
public class AisleController {
    private final AisleService aisleService;

    @GetMapping("/all")
    public ResponseEntity<Page<AisleDto>> getAllAisles(Pageable pageable){
        return ResponseEntity.ok(aisleService.getAllAisles(pageable));
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<AisleDto> getAisleById(@PathVariable Long id){
        return ResponseEntity.ok(aisleService.getAisleById(id));
    }
    @GetMapping("/code/{code}")
    public ResponseEntity<AisleDto> getAisleByCode(@PathVariable String code){
        return ResponseEntity.ok(aisleService.getAisleByCode(code));
    }
    @PostMapping("/create")
    public ResponseEntity<AisleDto> createAisle(@Valid @RequestBody AisleDto aisleDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(aisleService.createAisle(aisleDto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<AisleDto> updateAisle(@PathVariable Long id,@Valid @RequestBody AisleDto aisleDto){
        return ResponseEntity.ok(aisleService.updateAisle(id, aisleDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAisle(@PathVariable Long id){
        aisleService.deleteAisleById(id);
        return ResponseEntity.noContent().build();
    }
}
