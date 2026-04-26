package com.example.ucitelnice.service.impl;

import com.example.ucitelnice.dto.order.OrderResponse;
import com.example.ucitelnice.entity.Order;
import com.example.ucitelnice.mapper.OrderMapper;
import com.example.ucitelnice.repository.OrderRepository;
import com.example.ucitelnice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponse> allOrders() {

        List<Order> orders = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return orders.stream().map(orderMapper::toResponse).toList();
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {

        var order = orderRepository.findById(orderId).orElseThrow();
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getOrderByIdWithConfirmationCode(String confirmationCode) {

        var order = orderRepository.findByConfirmationToken(confirmationCode).orElseThrow();
        return orderMapper.toResponse(order);
    }
}
