package com.example.prictice_task_2025.service.tests;

import com.example.prictice_task_2025.dto.repsonse.OrderResponseDto;
import com.example.prictice_task_2025.dto.request.OrderRequestDto;
import com.example.prictice_task_2025.entity.Order;
import com.example.prictice_task_2025.entity.User;
import com.example.prictice_task_2025.repository.OrderRepository;
import com.example.prictice_task_2025.repository.UserRepository;
import com.example.prictice_task_2025.service.impl.OrderServiceImpl;
import com.example.prictice_task_2025.util.OrderRequestDtoToOrderConverter;
import com.example.prictice_task_2025.util.OrderToOrderResponseDtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private OrderToOrderResponseDtoConverter toResponseDtoConverter;
    private OrderRequestDtoToOrderConverter fromRequestDtoConverter;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        userRepository = mock(UserRepository.class);
        toResponseDtoConverter = mock(OrderToOrderResponseDtoConverter.class);
        fromRequestDtoConverter = mock(OrderRequestDtoToOrderConverter.class);

        orderService = new OrderServiceImpl(
                orderRepository,
                userRepository,
                toResponseDtoConverter,
                fromRequestDtoConverter
        );
    }

    @Test
    void createOrder_ShouldSaveAndReturnDto() {
        User user = new User();
        user.setId(1);

        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(1L);

        Order order = new Order();
        order.setUser(user);

        Order savedOrder = new Order();
        savedOrder.setId(10L);
        savedOrder.setUser(user);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(fromRequestDtoConverter.convert(requestDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(toResponseDtoConverter.convert(savedOrder)).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(requestDto);

        assertNotNull(result);
        assertEquals(10L, result.getId());

        verify(userRepository).findById(1L);
        verify(fromRequestDtoConverter).convert(requestDto);
        verify(orderRepository).save(order);
        verify(toResponseDtoConverter).convert(savedOrder);
    }

    @Test
    void createOrder_ShouldThrow_WhenUserNotFound() {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            orderService.createOrder(requestDto);
        });

        verify(userRepository).findById(99L);
        verifyNoMoreInteractions(orderRepository, fromRequestDtoConverter, toResponseDtoConverter);
    }

    @Test
    void getOrderById_ShouldReturnDto_WhenOrderExists() {
        Order order = new Order();
        order.setId(5L);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(5L);

        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(toResponseDtoConverter.convert(order)).thenReturn(responseDto);

        OrderResponseDto result = orderService.getOrderById(5L);

        assertNotNull(result);
        assertEquals(5L, result.getId());

        verify(orderRepository).findById(5L);
        verify(toResponseDtoConverter).convert(order);
    }

    @Test
    void getOrderById_ShouldThrow_WhenOrderNotFound() {
        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(100L));

        verify(orderRepository).findById(100L);
        verifyNoMoreInteractions(toResponseDtoConverter);
    }

    @Test
    void getAllOrders_ShouldReturnListOfDtos() {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        OrderResponseDto dto1 = new OrderResponseDto();
        dto1.setId(1L);
        OrderResponseDto dto2 = new OrderResponseDto();
        dto2.setId(2L);

        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));
        when(toResponseDtoConverter.convert(order1)).thenReturn(dto1);
        when(toResponseDtoConverter.convert(order2)).thenReturn(dto2);

        List<OrderResponseDto> results = orderService.getAllOrders(Pageable.unpaged());

        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());

        verify(orderRepository).findAll();
        verify(toResponseDtoConverter, times(2)).convert(any());
    }

    @Test
    void getOrdersByUserId_ShouldReturnList_WhenUserExists() {
        User user = new User();
        user.setId(1);

        Order order1 = new Order();
        order1.setId(10L);
        Order order2 = new Order();
        order2.setId(20L);

        user.setOrders(List.of(order1, order2));

        OrderResponseDto dto1 = new OrderResponseDto();
        dto1.setId(10L);
        OrderResponseDto dto2 = new OrderResponseDto();
        dto2.setId(20L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(toResponseDtoConverter.convert(order1)).thenReturn(dto1);
        when(toResponseDtoConverter.convert(order2)).thenReturn(dto2);

        List<OrderResponseDto> results = orderService.getOrdersByUserId(1L);

        assertEquals(2, results.size());
        assertEquals(10L, results.get(0).getId());
        assertEquals(20L, results.get(1).getId());

        verify(userRepository).findById(1L);
        verify(toResponseDtoConverter, times(2)).convert(any());
    }

    @Test
    void getOrdersByUserId_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            orderService.getOrdersByUserId(50L);
        });

        verify(userRepository).findById(50L);
        verifyNoMoreInteractions(toResponseDtoConverter);
    }

    @Test
    void deleteOrder_ShouldDelete_WhenOrderExists() {
        Order order = new Order();
        order.setId(15L);

        when(orderRepository.findById(15L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(15L);

        verify(orderRepository).findById(15L);
        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_ShouldThrow_WhenOrderNotFound() {
        when(orderRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> orderService.deleteOrder(123L));
        assertEquals("Order not found with id: 123", ex.getMessage());

        verify(orderRepository).findById(123L);
        verify(orderRepository, never()).delete(any());
    }

    @Test
    void deleteAllOrders_ShouldCallDeleteAll() {
        orderService.deleteAllOrders();

        verify(orderRepository).deleteAll();
    }
}
