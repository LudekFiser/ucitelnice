package com.example.ucitelnice.controller;

import com.example.ucitelnice.dto.orderItem.OrderItemResponse;
import com.example.ucitelnice.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order_items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping("/{orderId}/order-items")
    public ResponseEntity<List<OrderItemResponse>> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        var orderItems = orderItemService.getAllOrderItemsByOrderId(orderId);
        return ResponseEntity.ok().body(orderItems);
    }


}
