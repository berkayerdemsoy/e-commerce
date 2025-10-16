package com.example.shop_service_client.client;

import com.example.shop_service_client.dto.ProductCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "shop-service",
        contextId = "productClient",
        path = "/shop/product",
        url = "http://api-gateway:8080"
)
public interface ProductClient {

    @GetMapping("/id/{id}")
    ProductCreateDto getProductById(@PathVariable("id") Long id);

    @GetMapping("/name/{name}")
    ProductCreateDto getProductByName(@PathVariable("name") String name);

    @PostMapping("/create")
    ProductCreateDto addProduct(@RequestBody ProductCreateDto dto);

    @PutMapping("/{id}")
    ProductCreateDto updateProduct(@PathVariable("id") Long id,
                                   @RequestBody ProductCreateDto dto);

    @DeleteMapping("/{id}")
    void deleteProductById(@PathVariable("id") Long id);

    @GetMapping("/all")
    Page<ProductCreateDto> getAllProducts(Pageable pageable);
}
