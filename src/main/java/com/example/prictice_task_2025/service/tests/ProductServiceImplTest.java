package com.example.prictice_task_2025.service.tests;

import com.example.prictice_task_2025.dto.repsonse.ProductResponseDto;
import com.example.prictice_task_2025.dto.request.ProductRequestDto;
import com.example.prictice_task_2025.entity.Category;
import com.example.prictice_task_2025.entity.Product;
import com.example.prictice_task_2025.repository.CategoryRepository;
import com.example.prictice_task_2025.repository.ProductRepository;
import com.example.prictice_task_2025.service.impl.ProductServiceImpl;
import com.example.prictice_task_2025.util.ProductRequestDtoToProductConverter;
import com.example.prictice_task_2025.util.ProductToProductResponseDtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductRequestDtoToProductConverter requestDtoToProductConverter;
    private ProductToProductResponseDtoConverter toResponseDtoConverter;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        requestDtoToProductConverter = mock(ProductRequestDtoToProductConverter.class);
        toResponseDtoConverter = mock(ProductToProductResponseDtoConverter.class);

        productService = new ProductServiceImpl(
                productRepository,
                categoryRepository,
                requestDtoToProductConverter,
                toResponseDtoConverter
        );
    }

    @Test
    void createProduct_ShouldSaveAndReturnDto() {
        ProductRequestDto requestDto = new ProductRequestDto();
        Product product = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(1L);
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);

        when(requestDtoToProductConverter.convert(requestDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(toResponseDtoConverter.convert(savedProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.createProduct(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(requestDtoToProductConverter).convert(requestDto);
        verify(productRepository).save(product);
        verify(toResponseDtoConverter).convert(savedProduct);
    }

    @Test
    void updateProduct_ShouldUpdateAndReturnDto_WhenProductAndCategoryExist() {
        Long productId = 1L;
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setCategoryId(2L);
        requestDto.setName("New Name");
        requestDto.setDescription("New Description");
        requestDto.setPrice(10.0);

        Product existingProduct = new Product();
        existingProduct.setId(productId);

        Category category = new Category();
        category.setId(2L);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));
        when(productRepository.save(existingProduct)).thenReturn(updatedProduct);
        when(toResponseDtoConverter.convert(updatedProduct)).thenReturn(responseDto);

        ProductResponseDto result = productService.updateProduct(productId, requestDto);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("New Name", existingProduct.getName());
        assertEquals("New Description", existingProduct.getDescription());
        assertEquals(10.0, existingProduct.getPrice());
        assertEquals(category, existingProduct.getCategory());

        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(2L);
        verify(productRepository).save(existingProduct);
        verify(toResponseDtoConverter).convert(updatedProduct);
    }

    @Test
    void updateProduct_ShouldThrow_WhenProductNotFound() {
        Long productId = 1L;
        ProductRequestDto requestDto = new ProductRequestDto();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(productId, requestDto));

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(categoryRepository, productRepository, toResponseDtoConverter);
    }

    @Test
    void updateProduct_ShouldThrow_WhenCategoryNotFound() {
        Long productId = 1L;
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setCategoryId(10L);

        Product existingProduct = new Product();
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(productId, requestDto));

        verify(productRepository).findById(productId);
        verify(categoryRepository).findById(10L);
        verifyNoMoreInteractions(productRepository, toResponseDtoConverter);
    }

    @Test
    void deleteProduct_ShouldDelete_WhenProductExists() {
        Long productId = 5L;
        Product existingProduct = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.deleteProduct(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void deleteProduct_ShouldThrow_WhenProductNotFound() {
        Long productId = 5L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.deleteProduct(productId));

        verify(productRepository).findById(productId);
        verify(productRepository, never()).deleteById(productId);
    }

    @Test
    void getProductById_ShouldReturnDto_WhenFound() {
        Long productId = 7L;
        Product product = new Product();
        product.setId(productId);
        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(toResponseDtoConverter.convert(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());

        verify(productRepository).findById(productId);
        verify(toResponseDtoConverter).convert(product);
    }

    @Test
    void getProductById_ShouldThrow_WhenNotFound() {
        Long productId = 8L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.getProductById(productId));

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(toResponseDtoConverter);
    }

    @Test
    void getAllProducts_ShouldReturnListOfDtos() {
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        ProductResponseDto dto1 = new ProductResponseDto();
        dto1.setId(1L);
        ProductResponseDto dto2 = new ProductResponseDto();
        dto2.setId(2L);

        when(productRepository.findAll()).thenReturn(List.of(product1, product2));
        when(toResponseDtoConverter.convert(product1)).thenReturn(dto1);
        when(toResponseDtoConverter.convert(product2)).thenReturn(dto2);

        List<ProductResponseDto> result = productService.getAllProducts(Pageable.unpaged());

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());

        verify(productRepository).findAll();
        verify(toResponseDtoConverter, times(2)).convert(any());
    }

    @Test
    void getProductsByCategory_ShouldReturnList_WhenCategoryExists() {
        Long categoryId = 3L;

        Category category = new Category();
        category.setId(categoryId);

        Product product1 = new Product();
        product1.setId(10L);
        Product product2 = new Product();
        product2.setId(20L);

        category.setProducts(List.of(product1, product2));

        ProductResponseDto dto1 = new ProductResponseDto();
        dto1.setId(10L);
        ProductResponseDto dto2 = new ProductResponseDto();
        dto2.setId(20L);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(toResponseDtoConverter.convert(product1)).thenReturn(dto1);
        when(toResponseDtoConverter.convert(product2)).thenReturn(dto2);

        List<ProductResponseDto> result = productService.getProductsByCategory(categoryId);

        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getId());
        assertEquals(20L, result.get(1).getId());

        verify(categoryRepository).findById(categoryId);
        verify(toResponseDtoConverter, times(2)).convert(any());
    }

    @Test
    void getProductsByCategory_ShouldThrow_WhenCategoryNotFound() {
        Long categoryId = 4L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.getProductsByCategory(categoryId));

        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(toResponseDtoConverter);
    }
}
