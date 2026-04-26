package com.example.ucitelnice.controller;

import com.example.ucitelnice.dto.order.OrderResponse;
import com.example.ucitelnice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<OrderResponse>> allOrders() {
        var orders = orderService.allOrders();
        return ResponseEntity.ok().body(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        var order = orderService.getOrderById(orderId);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<OrderResponse> getOrderByIdWithConfirmationCode(@PathVariable String confirmationCode) {
        var order = orderService.getOrderByIdWithConfirmationCode(confirmationCode);
        return ResponseEntity.ok().body(order);
    }
}
