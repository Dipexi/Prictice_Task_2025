package com.example.prictice_task_2025.util;

import com.example.prictice_task_2025.dto.repsonse.CategoryResponseDto;
import com.example.prictice_task_2025.entity.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryToCategoryResponseDtoConverter implements Converter<Category, CategoryResponseDto> {

    @Override
    public CategoryResponseDto convert(Category source) {
       return CategoryResponseDto.builder()
               .id(source.getId())
               .name(source.getName())
               .description(source.getDescription())
               .build();
    }
}
