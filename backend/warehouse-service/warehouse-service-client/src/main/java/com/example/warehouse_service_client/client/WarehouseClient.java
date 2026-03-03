package com.example.warehouse_service_client.client;

import com.example.warehouse_service_client.dto.WarehouseDto;
import com.example.warehouse_service_client.dto.WarehouseSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "warehouse-service",
        contextId = "warehouseClient",
        path = "/api/warehouse",
        url = "http://api-gateway:8080"
)
public interface WarehouseClient {

    @GetMapping("/all")
    Page<WarehouseDto> getAllWarehouses();

    @GetMapping("/id/{id}")
    WarehouseDto getWarehouseById(@PathVariable("id") Long id);

    @GetMapping("/name/{name}")
    WarehouseDto getWarehouseByName(@PathVariable("name") String name);

    @PostMapping("/create")
    WarehouseDto createWarehouse(@RequestBody WarehouseDto dto);

    @PutMapping("/{id}")
    WarehouseDto updateWarehouse(@PathVariable("id") Long id, @RequestBody WarehouseDto dto);

    @DeleteMapping("/{id}")
    void deleteWarehouse(@PathVariable("id") Long id);

    @GetMapping("/{id}/summary")
    WarehouseSummaryDto getWarehouseSummary(@PathVariable("id") Long id);
}
