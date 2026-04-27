package com.example.ucitelnice.dto.cart;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long productId;
    private String title;
    private BigDecimal price;
}
