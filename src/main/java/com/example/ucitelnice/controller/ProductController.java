package com.example.ucitelnice.controller;

import com.example.ucitelnice.dto.product.CreateProductRequest;
import com.example.ucitelnice.dto.product.CreateProductResponse;
import com.example.ucitelnice.dto.product.UpdateProductRequest;
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
    @GetMapping("/all-admin")
    public ResponseEntity<List<CreateProductResponse>> allAdminProducts() {
        var products = productService.allAdminProducts();
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<CreateProductResponse> getProductById(@PathVariable Long productId) {
        var product = productService.getProductById(productId);
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/edit/{productId}")
    public ResponseEntity<CreateProductResponse> editProduct(
            @PathVariable Long productId,
            @RequestPart("req")UpdateProductRequest req,
            @RequestPart(name = "images", required = false) List<MultipartFile> images) {
        var product = productService.editProduct(productId, req, images);
        return ResponseEntity.ok().body(product);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}/deactivate")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long productId) {
        productService.deactivateProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
