package com.example.prictice_task_2025.util;

import com.example.prictice_task_2025.dto.repsonse.CategoryResponseDto;
import com.example.prictice_task_2025.dto.repsonse.ProductResponseDto;
import com.example.prictice_task_2025.entity.Category;
import com.example.prictice_task_2025.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToProductResponseDtoConverter implements Converter<Product, ProductResponseDto> {
    @Override
    public ProductResponseDto convert(Product source) {
        Category category = source.getCategory();
        CategoryResponseDto categoryResponseDto = null;

        if (category != null) {
            categoryResponseDto = CategoryResponseDto.builder()
                    .id(category.getId())
                    .description(category.getDescription())
                    .name(category.getName())
                    .build();
        }

        return ProductResponseDto.builder()
                .id(source.getId())
                .price(source.getPrice())
                .name(source.getName())
                .description(source.getDescription())
                .category(categoryResponseDto)
                .image(source.getImage())
                .build();
    }
}
