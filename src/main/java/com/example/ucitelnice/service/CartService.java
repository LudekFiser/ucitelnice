package com.example.ucitelnice.service;

import com.example.ucitelnice.dto.cart.CartItemResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface CartService {

    void addToCart(Long productId, HttpSession session);
    //void decreaseQuantity(Long productId, HttpSession session);
    List<CartItemResponse> getCart(HttpSession session);
}
