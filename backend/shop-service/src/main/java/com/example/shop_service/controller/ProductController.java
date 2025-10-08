package com.example.shop_service.controller;

import com.example.shop_service.dto.ProductCreateDto;
import com.example.shop_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductCreateDto> getProductById(@PathVariable Long id){
        ProductCreateDto dto = productService.getProductById(id);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductCreateDto> getProductByName(@PathVariable String name){
        ProductCreateDto dto = productService.getProductByName(name);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/create")
    public ResponseEntity<ProductCreateDto> addProduct(@Valid @RequestBody ProductCreateDto dto){
        ProductCreateDto product = productService.addProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductCreateDto> updateProduct(@PathVariable Long id ,@RequestBody ProductCreateDto dto){
        ProductCreateDto productCreateDto = productService.updateProduct(id,dto);
        return ResponseEntity.ok(productCreateDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all")
    public ResponseEntity<Page<ProductCreateDto>> getAllProducts(Pageable pageable){
        Page<ProductCreateDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }
}
