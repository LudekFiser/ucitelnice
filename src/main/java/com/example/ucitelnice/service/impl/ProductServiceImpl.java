package com.example.ucitelnice.service.impl;

import com.example.ucitelnice.cloudinary.CloudinaryService;
import com.example.ucitelnice.dto.image.UploadedImageDto;
import com.example.ucitelnice.dto.product.CreateProductRequest;
import com.example.ucitelnice.dto.product.CreateProductResponse;
import com.example.ucitelnice.mapper.ProductMapper;
import com.example.ucitelnice.repository.ProductRepository;
import com.example.ucitelnice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public CreateProductResponse createProduct(CreateProductRequest req, List<MultipartFile> images) {
        var product = productMapper.toEntity(req);

        if (images == null || images.isEmpty()) {
            images = List.of();
        }
        if (!images.isEmpty()) {
            if (images.size() > 10)
                throw new IllegalArgumentException("You can't upload more than 10 images");

            for (MultipartFile image : images) {
                UploadedImageDto uploadedImage = cloudinaryService.uploadImage(image, "product_images");
                var productImage = cloudinaryService.buildProjectImage(uploadedImage, product);
                product.addImage(productImage);
            }
        }
        product.setActive(true);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    public List<CreateProductResponse> allProducts() {
        var products = productRepository.findAll();
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Override
    public CreateProductResponse getProductById(Long productId) {
        var product = productRepository.findById(productId).orElseThrow();
        return productMapper.toResponse(product);
    }
}
