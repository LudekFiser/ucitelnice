package com.example.ucitelnice.mapper;

import com.example.ucitelnice.dto.image.ProductImageDto;
import com.example.ucitelnice.dto.product.CreateProductRequest;
import com.example.ucitelnice.dto.product.CreateProductResponse;
import com.example.ucitelnice.entity.Product;
import com.example.ucitelnice.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    Product toEntity(CreateProductRequest req);
    CreateProductResponse toResponse(Product product);

    ProductImageDto toImageDto(ProductImage image);
    List<ProductImageDto> toImageDtoList(List<ProductImage> images);
}
