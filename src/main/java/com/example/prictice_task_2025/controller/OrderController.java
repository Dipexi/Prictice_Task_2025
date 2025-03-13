package com.example.prictice_task_2025.controller;

import com.example.prictice_task_2025.dto.repsonse.OrderResponseDto;
import com.example.prictice_task_2025.dto.request.OrderRequestDto;
import com.example.prictice_task_2025.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody @Valid OrderRequestDto requestOrder) {
        OrderResponseDto createdOrder = orderService.createOrder(requestOrder);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        OrderResponseDto order = orderService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sort", defaultValue = "id") String sortField
    ) {
        List<OrderResponseDto> orders = orderService.getAllOrders(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortField)));
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
