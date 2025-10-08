package com.example.shop_service.serviceImpl;

import com.example.shop_service.dto.CategoryDto;
import com.example.shop_service.dto.ProductCreateDto;
import com.example.shop_service.entity.Category;
import com.example.shop_service.entity.Product;
import com.example.shop_service.exception.AlreadyExistsException;
import com.example.shop_service.exception.NotFoundException;
import com.example.shop_service.mapper.ProductMapper;
import com.example.shop_service.repository.CategoryRepository;
import com.example.shop_service.repository.ProductRepository;
import com.example.shop_service.service.ProductService;
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
        if (productRepository.findByName(dto.getName()).isPresent()){
            throw new AlreadyExistsException("Product already exists");
        }

        Category category = categoryRepository.findById(dto.getCategory_id().getId()).orElseThrow(() -> new NotFoundException("Category Not Found"));


        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .description(dto.getDescription())
                .category_id(category)
                .build();

        Product saved = productRepository.save(product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductCreateDto updateProduct(Long id, ProductCreateDto dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
        Category category = categoryRepository.findById(dto.getCategory_id().getId()).orElseThrow(() -> new NotFoundException("Category Not Found"));

        product.setDescription(dto.getDescription());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setCategory_id(category);
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
        Product product = productRepository.findByName(name).orElseThrow(() -> new NotFoundException("Product not found"));
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
