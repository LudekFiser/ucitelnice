package com.example.ucitelnice.service.impl;

import com.example.ucitelnice.dto.orderItem.OrderItemResponse;
import com.example.ucitelnice.mapper.OrderItemMapper;
import com.example.ucitelnice.repository.OrderItemRepository;
import com.example.ucitelnice.repository.OrderRepository;
import com.example.ucitelnice.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderItemResponse> getAllOrderItemsByOrderId(Long orderId) {
        var orderItems = orderItemRepository.findAllByOrder_Id(orderId);
        return orderItems.stream().map(orderItemMapper::toResponse).toList();
    }
}
