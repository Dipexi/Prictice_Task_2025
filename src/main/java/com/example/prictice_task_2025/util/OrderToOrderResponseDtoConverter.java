package com.example.prictice_task_2025.util;

import com.example.prictice_task_2025.dto.repsonse.OrderItemResponseDto;
import com.example.prictice_task_2025.dto.repsonse.OrderResponseDto;
import com.example.prictice_task_2025.dto.repsonse.ProductResponseDto;
import com.example.prictice_task_2025.entity.Order;
import com.example.prictice_task_2025.entity.OrderItem;
import com.example.prictice_task_2025.entity.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderToOrderResponseDtoConverter implements Converter<Order, OrderResponseDto> {

    @Override
    public OrderResponseDto convert(Order source) {
        List<OrderItemResponseDto> orderItemResponseDtos = source.getOrders().stream()
                .map(this::convertOrderItem)
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .id(source.getId())
                .orderDate(source.getOrderDate())
                .totalAmount(source.getTotalAmount())
                .orderItems(orderItemResponseDtos)
                .build();
    }

    private OrderItemResponseDto convertOrderItem(OrderItem orderItem) {
        return OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .productResponseDto(convertProduct(orderItem.getProduct()))
                .build();
    }

    private ProductResponseDto convertProduct(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .build();
    }
}
