package com.example.prictice_task_2025.service.impl;

import com.example.prictice_task_2025.dto.repsonse.OrderResponseDto;
import com.example.prictice_task_2025.dto.request.OrderRequestDto;
import com.example.prictice_task_2025.entity.Order;
import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.repository.OrderRepository;
import com.example.prictice_task_2025.repository.UserRepository;
import com.example.prictice_task_2025.service.OrderService;
import com.example.prictice_task_2025.util.OrderRequestDtoToOrderConverter;
import com.example.prictice_task_2025.util.OrderToOrderResponseDtoConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderToOrderResponseDtoConverter orderToOrderResponseDtoConverter;
    private final OrderRequestDtoToOrderConverter orderRequestDtoToOrderConverter;

    @Override
    public OrderResponseDto createOrder(@Valid OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.getUserId()).orElseThrow(NoSuchElementException::new);
        Order order = orderRepository.save(orderRequestDtoToOrderConverter.convert(orderRequestDto));
        order.setUser(user);
        return orderToOrderResponseDtoConverter.convert(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderToOrderResponseDtoConverter::convert)
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll()
                .stream()
                .map(orderToOrderResponseDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NoSuchElementException::new)
                .getOrders()
                .stream()
                .map(orderToOrderResponseDtoConverter::convert)
                .collect(Collectors.toList());
    }
}
