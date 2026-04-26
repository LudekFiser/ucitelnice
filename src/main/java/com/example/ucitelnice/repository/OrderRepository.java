package com.example.ucitelnice.repository;

import com.example.ucitelnice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStripeCheckoutSessionId(String stripeCheckoutSessionId);

    Optional<Order> findByConfirmationToken(String confirmationToken);

}
