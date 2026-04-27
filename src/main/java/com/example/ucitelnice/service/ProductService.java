package com.example.ucitelnice.service;

import com.example.ucitelnice.dto.product.CreateProductRequest;
import com.example.ucitelnice.dto.product.CreateProductResponse;
import com.example.ucitelnice.dto.product.UpdateProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {


    CreateProductResponse createProduct(CreateProductRequest req, List<MultipartFile> images);

    List<CreateProductResponse> allProducts();
    List<CreateProductResponse> allAdminProducts();

    CreateProductResponse getProductById(Long productId);

    CreateProductResponse editProduct(Long productId, UpdateProductRequest req, List<MultipartFile> images);
    void deleteProduct(Long productId);
    void deactivateProduct(Long productId);
}
