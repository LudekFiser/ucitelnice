package com.example.ucitelnice.service;

import com.example.ucitelnice.entity.OrderItem;
import com.example.ucitelnice.entity.Product;
import com.example.ucitelnice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final ProductRepository productRepository;


    @Value("${spring.mail.properties.mail.smtp.from}")
    private String sentBy;

    public void sendBoughtProductsLinks(String toEmail, Set<OrderItem> products, String confirmationToken) {
        var message = new SimpleMailMessage();
        message.setFrom(sentBy);
        message.setTo(toEmail);
        message.setSubject("Thank you for your purchase!");

        StringBuilder body = new StringBuilder();
        body.append("Thank you for your purchase!\n\n");
        body.append("View your ORDER Details here: " + "http://localhost:8081/orders/confirmation/" + confirmationToken + "\n\nYour products:\n\n");

        for (OrderItem orderItem : products) {
            var product = orderItem.getProduct();

            body.append("Product: ")
                    .append(product.getTitle())
                    .append("\n");

            body.append("Google Drive URL: ")
                    .append(product.getGoogleDriveUrl())
                    .append("\n\n");
        }

        message.setText(body.toString());
        mailSender.send(message);
    }
}
