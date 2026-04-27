package com.example.ucitelnice.controller;

import com.example.ucitelnice.dto.cart.CartItemResponse;
import com.example.ucitelnice.entity.Order;
import com.example.ucitelnice.entity.OrderItem;
import com.example.ucitelnice.enums.OrderStatus;
import com.example.ucitelnice.enums.PaymentStatus;
import com.example.ucitelnice.repository.OrderItemRepository;
import com.example.ucitelnice.repository.OrderRepository;
import com.example.ucitelnice.repository.ProductRepository;
import com.example.ucitelnice.service.CartService;
import com.example.ucitelnice.service.EmailService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @PostMapping("/api/payment/checkout")
    public String createCheckoutSession(HttpSession httpSession) throws StripeException {

        List<CartItemResponse> cart = cartService.getCart(httpSession);

        if (cart.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        var paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/payment/success")
                .setCancelUrl("http://localhost:5173/payment/cancelled");

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemResponse item : cart) {
            var lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency("czk")
                                    .setUnitAmount(
                                            item.getPrice()
                                                    .multiply(BigDecimal.valueOf(100))
                                                    .longValue()
                                    )
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                    .setName(item.getTitle())
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            paramsBuilder.addLineItem(lineItem);

            total = total.add(
                    item.getPrice()
            );
        }

        Order order = new Order();
        order.setOrderStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        order.setTotalAmount(total);
        order.setConfirmationToken(UUID.randomUUID().toString());

        order = orderRepository.save(order);

        for (CartItemResponse item : cart) {
            OrderItem orderItem = new OrderItem();

            var product = productRepository.findById(item.getProductId())
                    .orElseThrow();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setProductPrice(item.getPrice());
            orderItem.setProductTitle(item.getTitle());

            orderItemRepository.save(orderItem);
        }

        paramsBuilder.putMetadata("orderId", order.getId().toString());

        SessionCreateParams params = paramsBuilder.build();

        Session session = Session.create(params);

        order.setStripeCheckoutSessionId(session.getId());
        orderRepository.save(order);

        return session.getUrl();
    }

    @PostMapping("/api/payment/webhook")
    public String handleStripeWebhook(@RequestBody String payload,
                                      @RequestHeader("Stripe-Signature") String sigHeader) throws Exception {

        Event event;

        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    stripeWebhookSecret
            );
        } catch (SignatureVerificationException e) {
            return "invalid signature";
        }

        if ("checkout.session.completed".equals(event.getType())) {

            String rawJson = event.getDataObjectDeserializer().getRawJson();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(rawJson);

            String sessionId = jsonNode.get("id").asText();

            Session session = Session.retrieve(sessionId);

            Long orderId = Long.valueOf(session.getMetadata().get("orderId"));
            String customerEmail = session.getCustomerDetails().getEmail();

            Order order = orderRepository.findById(orderId)
                    .orElseThrow();
            if (order.getPaymentStatus() == PaymentStatus.PAID) {
                return "ok";
            }

            if (!session.getId().equals(order.getStripeCheckoutSessionId())) {
                throw new IllegalStateException("Stripe session ID does not match order");
            }

            order.setOrderStatus(OrderStatus.COMPLETED);
            order.setPaymentStatus(PaymentStatus.PAID);
            order.setStripePaymentIntentId(session.getPaymentIntent());
            order.setCustomerEmail(customerEmail);

            orderRepository.save(order);
            emailService.sendBoughtProductsLinks(customerEmail, order.getOrderItems(), order.getConfirmationToken());
        }


        return "ok";
    }
}

