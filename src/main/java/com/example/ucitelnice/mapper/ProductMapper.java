package com.example.ucitelnice.mapper;

import com.example.ucitelnice.dto.product.CreateProductRequest;
import com.example.ucitelnice.dto.product.CreateProductResponse;
import com.example.ucitelnice.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    Product toEntity(CreateProductRequest req);


    CreateProductResponse toResponse(Product product);
}
