package com.example.ucitelnice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductViewController {

    @GetMapping("/all")
    public String allProductsPage() {
        return "products/all-products";
    }

    @GetMapping("/new")
    public String newProductPage() {
        return "products/create-product";
    }

    @GetMapping("/{productId}")
    public String productDetailPage(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "products/product-detail";
    }
}
