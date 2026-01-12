package com.example.shop_service_app.serviceImpl;

import com.example.shop_service_client.dto.ProductCreateDto;
import com.example.shop_service_app.entity.Category;
import com.example.shop_service_app.entity.Product;
import com.example.shop_service_app.exception.AlreadyExistsException;
import com.example.shop_service_app.exception.NotFoundException;
import com.example.shop_service_app.mapper.ProductMapper;
import com.example.shop_service_app.repository.CategoryRepository;
import com.example.shop_service_app.repository.ProductRepository;
import com.example.shop_service_app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductCreateDto addProduct(ProductCreateDto dto) {
        if (productRepository.findByNameIgnoreCase(dto.getName()).isPresent()){
            throw new AlreadyExistsException("Product already exists");
        }

        Category category = categoryRepository.getReferenceById(dto.getCategoryId());


        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .category(category)
                .build();

        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    @Override
    public ProductCreateDto updateProduct(Long id, ProductCreateDto dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        Category category = categoryRepository.getReferenceById(dto.getCategoryId());


        product.setDescription(dto.getDescription());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory(category);
        Product updated = productRepository.save(product);
        return productMapper.toDto(updated);
    }

    @Override
    public ProductCreateDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        return productMapper.toDto(product);
    }

    @Override
    public ProductCreateDto getProductByName(String name) {
        Product product = productRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("Product not found"));
        return productMapper.toDto(product);
    }

    @Override
    public Page<ProductCreateDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toDto);
    }

    @Override
    public Void deleteProductById(Long id) {
         productRepository.deleteById(id);
         return null;
    }


}
