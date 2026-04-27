package com.example.ucitelnice.dto.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {


    @NotNull(message = "Title required")
    private String title;
    private String description;

    @NotNull(message = "Price required")
    @Size(min = 15, message = "Price must be greater than 15 Kč")
    private BigDecimal price;

    private String googleDriveUrl;
}
