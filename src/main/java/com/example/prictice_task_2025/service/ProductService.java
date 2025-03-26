package com.example.prictice_task_2025.service;

import com.example.prictice_task_2025.dto.repsonse.ProductResponseDto;
import com.example.prictice_task_2025.dto.request.ProductRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto requestDto);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productRequestDto);

    void deleteProduct(Long id);

    ProductResponseDto getProductById(Long id);

    List<ProductResponseDto> getAllProducts(Pageable pageable);

    List<ProductResponseDto> getProductsByCategory(Long id);
}
