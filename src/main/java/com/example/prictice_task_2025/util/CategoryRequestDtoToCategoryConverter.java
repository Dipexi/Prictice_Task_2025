package com.example.prictice_task_2025.util;

import com.example.prictice_task_2025.dto.request.CategoryRequestDto;
import com.example.prictice_task_2025.entity.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryRequestDtoToCategoryConverter implements Converter<CategoryRequestDto, Category> {

    @Override
    public Category convert(CategoryRequestDto source) {
        return Category.builder()
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }
}
