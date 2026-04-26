package com.example.ucitelnice.controller;

import com.example.ucitelnice.dto.product.CreateProductRequest;
import com.example.ucitelnice.dto.product.CreateProductResponse;
import com.example.ucitelnice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping("/create")
    public ResponseEntity<CreateProductResponse> createProduct(
            @RequestPart("request") @Valid CreateProductRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        var product = productService.createProduct(request, images);
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CreateProductResponse>> all() {
        var products = productService.allProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CreateProductResponse> getProductById(@PathVariable Long productId) {
        var product = productService.getProductById(productId);
        return ResponseEntity.ok().body(product);
    }
}
