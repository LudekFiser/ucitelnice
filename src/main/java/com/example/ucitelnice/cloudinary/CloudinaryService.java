package com.example.ucitelnice.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.ucitelnice.dto.image.UploadedImageDto;
import com.example.ucitelnice.entity.Product;
import com.example.ucitelnice.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public UploadedImageDto uploadImage(MultipartFile file, String folderName) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folderName,
                            "resource_type", "image"
                    )
            );

            String url = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            return new UploadedImageDto(url, publicId);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
    public ProductImage buildProjectImage(UploadedImageDto dto, Product product) {

        return ProductImage.builder()
                .imageUrl(dto.getUrl())
                .publicId(dto.getPublicId())
                .product(product)
                .build();
    }

    public void deleteImageByPublicId(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image with public_id: " + publicId, e);
        }
    }
}
