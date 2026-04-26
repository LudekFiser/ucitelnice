package com.example.ucitelnice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentPageController {

    @GetMapping("/payment/success")
    public String paymentSuccess(HttpSession session) {
        session.removeAttribute("CART");
        return "payment/success";
    }

    @GetMapping("/payment/cancelled")
    public String paymentCancelled() {
        return "payment/cancelled";
    }
}