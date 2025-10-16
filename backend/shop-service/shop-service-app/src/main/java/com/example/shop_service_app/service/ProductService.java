package com.example.shop_service_app.service;

import com.example.shop_service_client.dto.ProductCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductCreateDto addProduct(ProductCreateDto dto);
    ProductCreateDto updateProduct(Long id,ProductCreateDto dto);
    ProductCreateDto getProductById(Long id);
    ProductCreateDto getProductByName(String name);
    Page<ProductCreateDto> getAllProducts(Pageable pageable);
    Void deleteProductById(Long id);
}
