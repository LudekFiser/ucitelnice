package com.example.ucitelnice.dto.orderItem;

import com.example.ucitelnice.dto.order.OrderResponse;
import com.example.ucitelnice.entity.Product;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private BigDecimal productPrice;
    private String googleDriveUrl;
}
