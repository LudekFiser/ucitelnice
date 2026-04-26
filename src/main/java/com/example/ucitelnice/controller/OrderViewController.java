package com.example.ucitelnice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderViewController {

    @GetMapping("/all-orders")
    public String ordersPage() {
        return "order/orders";
    }

    @GetMapping("/{orderId}")
    public String orderDetailPage(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order/order-detail";
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public String orderDetailPage(@PathVariable String confirmationCode, Model model) {
        model.addAttribute("confirmationCode", confirmationCode);
        return "order/order-confirmation";
    }
}
