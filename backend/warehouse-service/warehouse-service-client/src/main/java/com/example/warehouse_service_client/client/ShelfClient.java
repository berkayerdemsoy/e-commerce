package com.example.warehouse_service_client.client;

import com.example.warehouse_service_client.dto.ShelfDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "warehouse-service",
        contextId = "shelfClient",
        path = "/api/shelf",
        url = "http://api-gateway:8080"

)
public interface ShelfClient {

    @GetMapping("/id/{id}")
    ShelfDto getShelfById(@PathVariable("id") Long id);

    @GetMapping("/code/{code}")
    ShelfDto getShelfByCode(@PathVariable("code") String code);

    @GetMapping("/all")
    Page<ShelfDto> getAllShelves();

    @PostMapping("/create")
    ShelfDto createShelf(@RequestBody ShelfDto dto);

    @PutMapping("/update/{id}")
    ShelfDto updateShelf(@PathVariable("id") Long id, @RequestBody ShelfDto dto);

    @DeleteMapping("/{id}")
    void deleteShelf(@PathVariable("id") Long id);
}
