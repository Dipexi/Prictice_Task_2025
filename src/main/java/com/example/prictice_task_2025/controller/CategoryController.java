package com.example.prictice_task_2025.controller;

import com.example.prictice_task_2025.dto.repsonse.CategoryResponseDto;
import com.example.prictice_task_2025.dto.request.CategoryRequestDto;
import com.example.prictice_task_2025.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(requestDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id,
                                                              @RequestBody @Valid CategoryRequestDto requestDto){
        CategoryResponseDto updatedCategory = categoryService.updateCategory(id, requestDto);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sortField
    ) {
        List<CategoryResponseDto> categories = categoryService.getAllCategories(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField)));
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
