package com.example.ucitelnice.dto.order;

import com.example.ucitelnice.enums.OrderStatus;
import com.example.ucitelnice.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {

    private Long id;
    private String customerEmail;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

}
