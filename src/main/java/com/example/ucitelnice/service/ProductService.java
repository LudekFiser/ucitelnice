package com.example.ucitelnice.service;

import com.example.ucitelnice.dto.product.CreateProductRequest;
import com.example.ucitelnice.dto.product.CreateProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {


    CreateProductResponse createProduct(CreateProductRequest req, List<MultipartFile> images);

    List<CreateProductResponse> allProducts();

    CreateProductResponse getProductById(Long productId);
}
