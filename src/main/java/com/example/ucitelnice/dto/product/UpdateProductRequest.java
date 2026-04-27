package com.example.ucitelnice.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateProductRequest {

    @NotNull(message = "Title required")
    private String title;

    private String description;

    @NotNull(message = "Price required")
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    private BigDecimal price;

    private String googleDriveUrl;

    List<Long> imageIdsToDelete;
}
