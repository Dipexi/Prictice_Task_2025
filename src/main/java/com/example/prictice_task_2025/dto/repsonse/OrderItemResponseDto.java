package com.example.prictice_task_2025.dto.repsonse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private String username;
    private Long userId;
    private Integer quantity;
    private Double price;
    private ProductResponseDto productResponseDto;
}
