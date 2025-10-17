package com.example.warehouse_service_client.client;

import com.example.warehouse_service_client.dto.AisleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "warehouse-service",
        contextId = "aisleClient",
        path = "/api/aisles",
        url = "http://api-gateway:8080"
)
public interface AisleClient {

    @GetMapping("/all")
    Page<AisleDto> getAllAisles(Pageable pageable);

    @GetMapping("/id/{id}")
    AisleDto getAisleById(@PathVariable("id") Long id);

    @GetMapping("/code/{code}")
    AisleDto getAisleByCode(@PathVariable("code") String code);

    @PostMapping("/create")
    AisleDto createAisle(@RequestBody AisleDto dto);

    @PutMapping("/update/{id}")
    AisleDto updateAisle(@PathVariable("id") Long id, @RequestBody AisleDto dto);

    @DeleteMapping("/{id}")
    void deleteAisle(@PathVariable("id") Long id);
}
