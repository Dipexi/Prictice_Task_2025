package com.example.prictice_task_2025.service;

import com.example.prictice_task_2025.dto.repsonse.OrderResponseDto;
import com.example.prictice_task_2025.dto.request.OrderRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);
    OrderResponseDto getOrderById(Long id);
    List<OrderResponseDto> getAllOrders(Pageable pageable);
    List<OrderResponseDto> getOrdersByUserId(Long userId);
}
