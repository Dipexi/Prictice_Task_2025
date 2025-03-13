package com.example.prictice_task_2025.util;

import com.example.prictice_task_2025.dto.request.OrderItemRequestDto;
import com.example.prictice_task_2025.dto.request.OrderRequestDto;
import com.example.prictice_task_2025.entity.Order;
import com.example.prictice_task_2025.entity.OrderItem;
import com.example.prictice_task_2025.entity.Product;
import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.repository.ProductRepository;
import com.example.prictice_task_2025.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRequestDtoToOrderConverter implements Converter<OrderRequestDto, Order> {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public Order convert(OrderRequestDto source) {
        User user = userRepository.findById(source.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + source.getUserId()));

        List<OrderItem> orderItems = source.getOrderItems().stream()
                .map(this::convertOrderItem)
                .collect(Collectors.toList());

        Order order = Order.builder()
                .user(user)
                .orderDate(source.getOrderDate())
                .totalAmount(source.getTotalAmount())
                .orders(orderItems)
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        return order;
    }

    private OrderItem convertOrderItem(OrderItemRequestDto orderItemRequestDto) {
        Product product = productRepository.findById(orderItemRequestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderItemRequestDto.getProductId()));

        return OrderItem.builder()
                .quantity(orderItemRequestDto.getQuantity())
                .price(orderItemRequestDto.getPrice())
                .product(product)
                .build();
    }
}
