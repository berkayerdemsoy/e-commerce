package com.example.warehouse_service_app.controller;

import com.example.warehouse_service_client.dto.WarehouseDto;
import com.example.warehouse_service_app.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;


    @GetMapping("/all")
    public ResponseEntity<Page<WarehouseDto>> getAllWarehouses(Pageable pageable){
        Page<WarehouseDto> warehouseDto = warehouseService.getAllWarehouses(pageable);
        return ResponseEntity.ok(warehouseDto);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable Long id){
        WarehouseDto dto = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<WarehouseDto> getWarehouseByName(@PathVariable String name){
        WarehouseDto dto = warehouseService.getWarehouseByName(name);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/create")
    public ResponseEntity<WarehouseDto> createWarehouse(@Valid @RequestBody WarehouseDto warehouseDto){
        WarehouseDto dto = warehouseService.createWarehouse(warehouseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable Long id,@Valid @RequestBody WarehouseDto warehouseDto){
        WarehouseDto dto = warehouseService.updateWarehouse(id,warehouseDto);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id){
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
