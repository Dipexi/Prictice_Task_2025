package com.example.prictice_task_2025.service.tests;

import com.example.prictice_task_2025.dto.repsonse.CategoryResponseDto;
import com.example.prictice_task_2025.dto.request.CategoryRequestDto;
import com.example.prictice_task_2025.entity.Category;
import com.example.prictice_task_2025.repository.CategoryRepository;
import com.example.prictice_task_2025.service.impl.CategoryServiceImpl;
import com.example.prictice_task_2025.util.CategoryRequestDtoToCategoryConverter;
import com.example.prictice_task_2025.util.CategoryToCategoryResponseDtoConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock private CategoryRepository categoryRepository;
    @Mock private CategoryToCategoryResponseDtoConverter toResponseDtoConverter;
    @Mock private CategoryRequestDtoToCategoryConverter toEntityConverter;

    @InjectMocks private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryRequestDto requestDto;
    private CategoryResponseDto responseDto;

    @BeforeEach
    void setup() {
        requestDto = new CategoryRequestDto("Books", "All kinds of books");
        category = new Category();
        category.setId(1L);
        category.setName("Books");
        category.setDescription("All kinds of books");

        responseDto = new CategoryResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Books");
        responseDto.setDescription("All kinds of books");
    }

    @Test
    void createCategory_shouldSaveAndReturnResponseDto() {
        when(toEntityConverter.convert(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(toResponseDtoConverter.convert(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.createCategory(requestDto);

        assertEquals(responseDto, result);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateCategory_shouldUpdateAndReturnResponseDto() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(toResponseDtoConverter.convert(any(Category.class))).thenReturn(responseDto);

        CategoryRequestDto updatedDto = new CategoryRequestDto("New Name", "New Description");
        CategoryResponseDto result = categoryService.updateCategory(1L, updatedDto);

        assertEquals(responseDto, result);
        assertEquals("New Name", category.getName());
        assertEquals("New Description", category.getDescription());
    }

    @Test
    void updateCategory_shouldThrowException_ifNotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> categoryService.updateCategory(2L, requestDto));
    }

    @Test
    void deleteCategory_shouldDelete_ifExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        categoryService.deleteCategory(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_shouldThrowException_ifNotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> categoryService.deleteCategory(2L));
    }

    @Test
    void getCategoryById_shouldReturnDto_ifExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(toResponseDtoConverter.convert(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getCategoryById(1L);
        assertEquals(responseDto, result);
    }

    @Test
    void getCategoryById_shouldThrowException_ifNotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> categoryService.getCategoryById(2L));
    }

    @Test
    void getAllCategories_shouldReturnListOfResponseDtos() {
        List<Category> categories = List.of(category);
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(categories));
        when(toResponseDtoConverter.convert(category)).thenReturn(responseDto);

        List<CategoryResponseDto> result = categoryService.getAllCategories(Pageable.unpaged());

        assertEquals(1, result.size());
        assertEquals(responseDto, result.get(0));
    }
}
