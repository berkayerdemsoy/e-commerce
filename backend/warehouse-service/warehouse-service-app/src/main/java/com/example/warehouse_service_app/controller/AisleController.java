package com.example.warehouse_service_app.controller;

import com.example.warehouse_service_client.dto.AisleDto;
import com.example.warehouse_service_app.service.AisleService;
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
    public ResponseEntity<AisleDto> getAisleById(@PathVariable("id") Long id){
        return ResponseEntity.ok(aisleService.getAisleById(id));
    }
    @GetMapping("/code/{code}")
    public ResponseEntity<AisleDto> getAisleByCode(@PathVariable("code") String code){
        return ResponseEntity.ok(aisleService.getAisleByCode(code));
    }
    @PostMapping("/create")
    public ResponseEntity<AisleDto> createAisle(@Valid @RequestBody AisleDto aisleDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(aisleService.createAisle(aisleDto));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<AisleDto> updateAisle(@PathVariable("id") Long id,@Valid @RequestBody AisleDto aisleDto){
        return ResponseEntity.ok(aisleService.updateAisle(id, aisleDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAisle(@PathVariable("id") Long id){
        aisleService.deleteAisleById(id);
        return ResponseEntity.noContent().build();
    }
}
