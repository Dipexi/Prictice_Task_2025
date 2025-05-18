package com.example.prictice_task_2025.service.impl;

import com.example.prictice_task_2025.dto.repsonse.ProductResponseDto;
import com.example.prictice_task_2025.dto.request.ProductRequestDto;
import com.example.prictice_task_2025.entity.Category;
import com.example.prictice_task_2025.entity.Product;
import com.example.prictice_task_2025.repository.CategoryRepository;
import com.example.prictice_task_2025.repository.ProductRepository;
import com.example.prictice_task_2025.service.ProductService;
import com.example.prictice_task_2025.util.ProductRequestDtoToProductConverter;
import com.example.prictice_task_2025.util.ProductToProductResponseDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class
ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRequestDtoToProductConverter productRequestDtoToProductConverter;
    private final ProductToProductResponseDtoConverter productToProductResponseDtoConverter;

    @Override
    public ProductResponseDto createProduct(ProductRequestDto productRequestDto) {
        Product product = productRepository
                .save(productRequestDtoToProductConverter.convert(productRequestDto));
        return productToProductResponseDtoConverter.convert(product);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(NoSuchElementException::new);

        product.setName(requestDto.getName());
        product.setDescription(requestDto.getDescription());
        product.setPrice(requestDto.getPrice());
        product.setCategory(category);


        Product updateProduct = productRepository.save(product);
        return productToProductResponseDtoConverter.convert(updateProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productToProductResponseDtoConverter::convert)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll()
                .stream()
                .map(productToProductResponseDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getProductsByCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);

        return category.getProducts().stream()
                .map(productToProductResponseDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
