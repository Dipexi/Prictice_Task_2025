package com.example.prictice_task_2025.controller;

import com.example.prictice_task_2025.dto.repsonse.ProductResponseDto;
import com.example.prictice_task_2025.dto.request.ProductRequestDto;
import com.example.prictice_task_2025.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto productRequestDto) {
        ProductResponseDto createdProduct = productService.createProduct(productRequestDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,
                                                            @RequestBody @Valid ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productRequestDto);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sortField
    ) {
        List<ProductResponseDto> products = productService.getAllProducts(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField)));
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponseDto> products = productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
