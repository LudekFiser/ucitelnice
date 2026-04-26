package com.example.ucitelnice.service;

import com.example.ucitelnice.dto.orderItem.OrderItemResponse;

import java.util.List;

public interface OrderItemService {


    List<OrderItemResponse> getAllOrderItemsByOrderId(Long orderId);
}
