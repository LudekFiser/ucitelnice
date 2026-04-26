package com.example.ucitelnice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CartViewController {

    @GetMapping("/cart")
    public String cartPage() {
        return "cart/cart";
    }


}