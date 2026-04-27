package com.example.ucitelnice.controller;

import com.example.ucitelnice.dto.cart.CartItemResponse;
import com.example.ucitelnice.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<String> addToCart(@PathVariable Long productId, HttpSession session) {
        cartService.addToCart(productId, session);
        return ResponseEntity.ok("Product added to cart");
    }
    /*@PostMapping("/decrease/{productId}")
    public ResponseEntity<Void> decreaseQuantity(@PathVariable Long productId, HttpSession session) {
        cartService.decreaseQuantity(productId, session);
        return ResponseEntity.ok().build();
    }*/
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(HttpSession session) {
        return ResponseEntity.ok(cartService.getCart(session));
    }
}