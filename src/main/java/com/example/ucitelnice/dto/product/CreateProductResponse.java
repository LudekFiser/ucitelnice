package com.example.ucitelnice.dto.product;

import com.example.ucitelnice.dto.image.ProductImageDto;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateProductResponse {

    private Long id;

    private String title;

    private String description;

    private BigDecimal price;

    private Boolean active;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private List<ProductImageDto> productImages;
}
