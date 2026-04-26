package com.example.ucitelnice.service;

import com.example.ucitelnice.dto.order.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> allOrders();
    OrderResponse getOrderById(Long orderId);
    OrderResponse getOrderByIdWithConfirmationCode(String confirmationCode);
}
