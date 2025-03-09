package com.example.prictice_task_2025.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ProductRequestDto {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @Min(value = 0)
    private Double price;
    @Min(value = 0)
    private Long categoryId;
    private String image;
}
