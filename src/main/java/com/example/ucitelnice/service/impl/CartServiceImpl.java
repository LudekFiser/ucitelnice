package com.example.ucitelnice.service.impl;

import com.example.ucitelnice.dto.cart.CartItemResponse;
import com.example.ucitelnice.repository.ProductRepository;
import com.example.ucitelnice.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private static final String CART_SESSION_KEY = "CART";

    @Override
    public void addToCart(Long productId, HttpSession session) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        var cart = getOrCreateCart(session);

        CartItemResponse existingItem = null;

        for (CartItemResponse item : cart) {
            if (item.getProductId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            cart.remove(existingItem);
        } else {
            CartItemResponse newItem = new CartItemResponse();
            newItem.setProductId(product.getId());
            newItem.setTitle(product.getTitle());
            newItem.setPrice(product.getPrice());

            cart.add(newItem);
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }

    /*@Override
    public void decreaseQuantity(Long productId, HttpSession session) {
        var cart = getOrCreateCart(session);

        CartItemResponse existingItem = null;

        for (CartItemResponse item : cart) {
            if (item.getProductId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem == null) {
            return;
        }

        if (existingItem.getQuantity() > 1) {
            existingItem.setQuantity(existingItem.getQuantity() - 1);
        } else {
            cart.remove(existingItem);
        }

        session.setAttribute(CART_SESSION_KEY, cart);
    }*/


    @Override
    public List<CartItemResponse> getCart(HttpSession session) {
        return getOrCreateCart(session);
    }

    @SuppressWarnings("unchecked")
    private List<CartItemResponse> getOrCreateCart(HttpSession session) {
        var cart = (List<CartItemResponse>) session.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }

        return cart;
    }
}
