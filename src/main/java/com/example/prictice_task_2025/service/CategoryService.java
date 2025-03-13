package com.example.prictice_task_2025.service;

import com.example.prictice_task_2025.dto.repsonse.CategoryResponseDto;
import com.example.prictice_task_2025.dto.request.CategoryRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto);
    void deleteCategory(Long id);
    CategoryResponseDto getCategoryById(Long id);
    List<CategoryResponseDto> getAllCategories(Pageable pageable);
}
