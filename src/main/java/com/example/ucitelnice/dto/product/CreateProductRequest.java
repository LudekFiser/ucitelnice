package com.example.ucitelnice.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {


    @NotNull(message = "Title required")
    private String title;
    private String description;
    @NotNull(message = "Price required")
    private BigDecimal price;

    private String googleDriveUrl;
}
